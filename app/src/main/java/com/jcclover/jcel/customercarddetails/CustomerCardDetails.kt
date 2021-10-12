package com.jcclover.jcel.customercarddetails

import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jcclover.R
import com.jcclover.jcel.webview.MyWebViewClient
import com.jcclover.jcel.network.ApiResponse

import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.*

import com.jcclover.jcel.repository.Repository
import com.jcclover.jcel.util.Constant.Companion.BASE_URL_WEBVIEW
import com.jcclover.jcel.util.log
import com.jcclover.jcel.util.toast
import com.jcclover.jcel.webview.WebAppInterFace
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*


class CustomerCardDetails : Fragment() {
    private lateinit var cardViewModel:CardDetailsViewModel
    private lateinit var  viewModel:MainViewModel
    var editor:SharedPreferences.Editor?=null
    var sharedpreferences: SharedPreferences? = null
    private  var chargeID:String?=null
    lateinit var processbar:ProgressBar
    private val args:CustomerCardDetailsArgs by navArgs()
    private  var payToken:String?=null
    var webView:WebView?=null
    lateinit var listenDisposable:Disposable
    var scope= CoroutineScope(CoroutineName("MyScope")+Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        sharedpreferences=requireActivity().getSharedPreferences("mylab", Context.MODE_PRIVATE)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        getActivity()?.setTitle("CloverPay");
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        cardViewModel=ViewModelProvider(this).get(CardDetailsViewModel::class.java)
        return inflater.inflate(com.jcclover.R.layout.fragment_customer_card_details, container, false)
    }

    override fun onStart() {
        super.onStart()
        requireContext().registerReceiver(paymentchargeswithToken, IntentFilter("Action_update_token"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView=view.findViewById(R.id.webView)
      processbar=view.findViewById(R.id.progressbar) as ProgressBar
                view.findViewById<WebView>(R.id.webView).apply {
                    settings.javaScriptEnabled=true
                        addJavascriptInterface(WebAppInterFace(requireContext()),"Android")
                        webViewClient = MyWebViewClient(requireContext())
                         loadUrl(BASE_URL_WEBVIEW)
                    }

        //Getting data from Webview
    listenDisposable = RxBus1.listen(RxEvent.PaymentToken::class.java).subscribe() {
                payToken = it.token
                val intent = Intent("Action_update_token")
                intent.putExtra("token", payToken)
                requireActivity().sendBroadcast(intent)
            }
    }



    private val paymentchargeswithToken:BroadcastReceiver=object :BroadcastReceiver(){

        override fun onReceive(ctx: Context?, intent: Intent?) {
            scope.launch{
                processbar.visibility=View.VISIBLE
                webView!!.visibility=View.GONE
                var paymentToken:String?=intent!!.getStringExtra("token")
                createChanges(paymentToken!!,args.amount,args.position)
            }
        }
    }




    private fun createChanges(paymentToken:String, amount:Int, position:Int) {
        viewModel.createChange(Charges("ecom",amount.toInt(),"usd",paymentToken))
        viewModel.mycreateCHangeResponsw.observe(requireActivity(), Observer { ApiResponse->
            when(ApiResponse){
                is ApiResponse.Success->{
                    chargeID=ApiResponse.response.id
                    RxBus1.publish(RxEvent.ResponseOrderId(chargeID!!,amount.toString(),position))
                    val action=CustomerCardDetailsDirections.actionCustomerCardDetailsToCustomerList()
                    findNavController().navigate(action)
                    processbar.visibility=View.GONE
                    requireActivity().toast("Payment is Successfull")
                }
                is ApiResponse.CustomError->{
                    processbar.visibility=View.GONE
                    requireActivity().toast("${ApiResponse.message}")
                    requireActivity().log("${ApiResponse.message}","ApiResponse")

                }
            }

        })
    }


    override fun onStop() {
        requireContext().unregisterReceiver(paymentchargeswithToken)
        super.onStop()
    }


    override fun onDestroy() {
        listenDisposable.dispose()
        super.onDestroy()
    }
}