package com.jcclover.jcel.ui

import android.accounts.Account
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.order.*
import com.jcclover.R
import com.jcclover.databinding.FragmentCustomerListBinding
import com.jcclover.jcel.Implementation.OnServiceClickListner
import com.jcclover.jcel.base.BaseDialogFragment
import com.jcclover.jcel.base.BaseFragment
import com.jcclover.jcel.base.BaseInterface
import com.jcclover.jcel.base.BaseViewModel
import com.jcclover.jcel.customerlist.CustomerListAdaptor
import com.jcclover.jcel.customerlist.InvoiceList
import com.jcclover.jcel.customerlist.customviewmodel.CustomerListViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.repository.Repository
import com.jcclover.jcel.screens.dialog.DialogManger
import com.jcclover.jcel.util.AlertDialogue
import com.jcclover.jcel.util.log
import com.jcclover.jcel.util.toast
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*


class CustomerList : BaseFragment<CustomerListViewModel,FragmentCustomerListBinding,BaseDialogFragment>(),OnServiceClickListner ,BaseInterface{
    private lateinit var rcCusList: RecyclerView
    private lateinit var paymentOrder: PaymentOrder
    private lateinit var adaptor: CustomerListAdaptor
    private lateinit var listenDisposable: Disposable
    var amount:String= MutableLiveData<String>().toString()
    var position:String= MutableLiveData<String>().toString()
    var scope= CoroutineScope(CoroutineName("MyCustomerlistscope") + Dispatchers.Main)
    lateinit var dialogManger:DialogManger
    companion object {
        var invoicelist = InvoiceList().createList()
    }

      override fun getViewModel()=CustomerListViewModel::class.java
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = FragmentCustomerListBinding.inflate(inflater, container, false)

    override fun getDialog(): BaseDialogFragment = BaseDialogFragment ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        rcCusList = binding.customerlistRecyclerView
        recyclerView()
        dialogManger = DialogManger(requireActivity(), requireActivity().supportFragmentManager)

        listenDisposable = RxBus1.listen(RxEvent.ResponseOrderId::class.java).subscribe {
            adaptor.notifyDataSetChanged().apply {
                invoicelist[it.position].uniqueIdentifiaction = it.orderId
                invoicelist[it.position].paymentStatus = "Paid"
                if (invoicelist[it.position].paymentStatus == "Paid") {
                    view.findViewById<Button>(R.id.btn_details).visibility = View.INVISIBLE
                }
            }
        }

        listenDisposable = RxBus1.listen(RxEvent.EventDialog::class.java).subscribe {
            when (it.dialogEvent) {
                RxEvent.DialogEventEnum.PAYMENT_PAGE -> {
                    try {
                        requireContext().log("position i clicked ${position.toInt()}","Customerlist")
                        val action =
                            CustomerListDirections.actionCustomerListToCustomerCardDetails(amount!!.toInt(),
                                position!!.toInt())
                        findNavController().navigate(action)
                        onDestroy()

                    }catch (e:Exception){
                        requireContext().log("error msg $e","Exception")
                    }
                }
               // RxEvent.DialogEventEnum.POSITIVE -> {

                }
              //  RxEvent.DialogEventEnum.NEGATIVE -> {

                }
            }



    private fun recyclerView() {
        adaptor = CustomerListAdaptor(invoicelist)
        rcCusList.layoutManager = LinearLayoutManager(activity)
        adaptor.setonClickListner(this)
        rcCusList.adapter = adaptor

    }


    override fun onServiceClicked(service: Any, position: Int) {
        if (service is PaymentOrder) {

            paymentOrder = PaymentOrder(service.uniqueIdentifiaction,
                service.customerName,
                service.customerid,
                service.amount,
                service.paymentStatus,
                service.expandable,
                service.orderId,
                service.invoiceNO)
           amount=service.amount
           this.position=position.toString()


   //alertmessageCustom(service.amount, position)

  //baseDialogFragment.show(requireActivity().supportFragmentManager,"Fragment")


dialogManger.showAppExitDialog(service.amount,position,"exit")

        }
    }


    fun alertmessageCustom(amount: String, position: Int) {
        val mDialogView =
            LayoutInflater.from(requireActivity()).inflate(R.layout.customdialog, null)
        val swipe: Button = mDialogView.findViewById(R.id.btn_cancel)
        val manual: Button = mDialogView.findViewById(R.id.btn_okay)
        val mBuilder = AlertDialog.Builder(requireActivity())
            .setView(mDialogView)
            .setTitle("CloverPay")
        val mAlertDialog = mBuilder.show()
        manual.setOnClickListener {
            requireActivity().toast("Manual card Entry")
            val action =
                CustomerListDirections.actionCustomerListToCustomerCardDetails(amount.toInt(),
                    position)
            findNavController().navigate(action)
            mAlertDialog.dismiss()
        }

        swipe.setOnClickListener {
            requireActivity().toast("Swipe Entry")
            var alertDialogue = AlertDialogue(requireActivity())
            alertDialogue.alertmessage()
            mAlertDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listenDisposable.dispose()
//        getDialog().dismiss()
        position=""
        requireActivity().log(" destroy position $position","Customerlist")
    }

    override fun success() {
        requireActivity().log(" in customer list sucess","BaseInterface")
    }

    override fun failure() {

    }


}





