package com.jcclover.jcel.ui

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jcclover.databinding.FragmentCustomerCardDetailsBinding
import com.jcclover.jcel.base.BaseDialogFragment
import com.jcclover.jcel.base.BaseFragment
import com.jcclover.jcel.customercarddetails.CardDetailsInterface
import com.jcclover.jcel.customercarddetails.CardDetailsViewModel
import com.jcclover.jcel.network.ApiResponse

import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.*

import com.jcclover.jcel.repository.Repository
import com.jcclover.jcel.util.log
import com.jcclover.jcel.util.toast
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CustomerCardDetails : BaseFragment<CardDetailsViewModel,FragmentCustomerCardDetailsBinding,BaseDialogFragment>(),
    CardDetailsInterface {
    var scope= CoroutineScope(CoroutineName("MyScope") + Dispatchers.Main)
   private lateinit var  viewModelMain:MainViewModel
    private  var chargeID:String?=null
    private val args:CustomerCardDetailsArgs by navArgs()
    private  var payToken:String?=null
    val repository = Repository()
    val viewModelFactory = MainViewModelFactory(repository)
    lateinit var listenDisposable:Disposable

    override fun getViewModel()= CardDetailsViewModel::class.java


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    )= FragmentCustomerCardDetailsBinding.inflate(inflater,container,false)


    override fun onStart() {
        super.onStart()
        requireContext().registerReceiver(paymentchargeswithToken, IntentFilter("Action_update_token"))
       // requireContext().registerReceiver(dialogresponse, IntentFilter("Action_dialogresponse"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getActivity()?.setTitle("CloverPay");
        viewModelMain = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.callwebView(binding,requireContext())

        //Getting data from Webview
       listenDisposable = RxBus1.listen(RxEvent.PaymentToken::class.java).subscribe() {
                payToken = it.token
                val intent = Intent("Action_update_token")
                intent.putExtra("token", payToken)
                requireActivity().sendBroadcast(intent)
            }
    }

    val paymentchargeswithToken: BroadcastReceiver =object : BroadcastReceiver(){

        override fun onReceive(ctx: Context?, intent: Intent?) {
            scope.launch{

                binding.progressbar.visibility= View.VISIBLE
                binding.webView.visibility= View.GONE
                var paymentToken:String?=intent!!.getStringExtra("token")
          createChanges(paymentToken!!,args.amount,args.position)

            }
        }
    }





    private fun createChanges(paymentToken:String, amount:Int, position:Int) {

        viewModelMain.createChange(Charges("ecom",amount.toInt(),"usd",paymentToken))
        viewModelMain.mycreateCHangeResponsw.observe(requireActivity(), Observer { ApiResponse->
            when(ApiResponse){
                is ApiResponse.Success->{
                    chargeID=ApiResponse.response.id

                //    RxBus1.publish(RxEvent.chargesSucess(chargeID!!,"Success"))
                    RxBus1.publish(RxEvent.ResponseOrderId(chargeID!!,amount.toString(),position))
                    requireContext().log("create changes$position","createCHanges")
                    val action=CustomerCardDetailsDirections.actionCustomerCardDetailsToCustomerList()
                    findNavController().navigate(action)
                    binding.progressbar.visibility=View.GONE
                    requireActivity().toast("Payment is Successfull")
                }
                is ApiResponse.CustomError->{
                    binding.progressbar.visibility=View.GONE
                    requireActivity().toast("${ApiResponse.message}")
                    requireActivity().log("${ApiResponse.message}","ApiResponse")
                }
            }

        })
    }


    override fun onStop() {
        requireContext().unregisterReceiver(paymentchargeswithToken)
      //  requireContext().unregisterReceiver(dialogresponse)
        super.onStop()
    }


    override fun onDestroy() {
        listenDisposable.dispose()
        super.onDestroy()
    }

    override fun sendingdata(paymentToken: String?) {
        createChanges(paymentToken!!,args.amount,args.position)
    }

    override fun getDialog()= BaseDialogFragment()

//    val dialogresponse: BroadcastReceiver =object : BroadcastReceiver(){
//
//        override fun onReceive(ctx: Context?, intent: Intent?) {
//            scope.launch{
//                var paymentToken:String?=intent!!.getStringExtra("token")
//                val action =
//                    CustomerListDirections.actionCustomerListToCustomerCardDetails(amount!!.toInt(),
//                        position!!)
//                findNavController().navigate(action)
//
//            }
//        }
//    }

}