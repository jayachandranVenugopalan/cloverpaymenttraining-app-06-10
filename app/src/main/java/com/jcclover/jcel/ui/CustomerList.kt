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

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.order.*
import com.jcclover.R
import com.jcclover.databinding.FragmentCustomerListBinding
import com.jcclover.jcel.Implementation.OnServiceClickListner
import com.jcclover.jcel.base.BaseFragment
import com.jcclover.jcel.customerlist.CustomerListAdaptor
import com.jcclover.jcel.customerlist.InvoiceList
import com.jcclover.jcel.customerlist.customviewmodel.CustomerListViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.repository.Repository
import com.jcclover.jcel.util.AlertDialogue
import com.jcclover.jcel.util.toast
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope


class CustomerList : BaseFragment<CustomerListViewModel,FragmentCustomerListBinding>(),OnServiceClickListner {
    private lateinit var rcCusList: RecyclerView
    private lateinit var paymentOrder: PaymentOrder
    lateinit var adaptor: CustomerListAdaptor
    var customerName: String? = null
    var customerid: String? = null
    var amount: String? = null
    var paystatus: String? = null
    var expandable: Boolean = false
    var invoiceno: String? = null

    lateinit var listenDisposable: Disposable


    companion object {
        var invoicelist = InvoiceList().createList()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        val repository = Repository()
//        val viewModelFactory = MainViewModelFactory(repository)
//        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
//        rcCusList = view.findViewById(R.id.customerlist_recycler_view)
        rcCusList = binding.customerlistRecyclerView
        recyclerView()

        listenDisposable = RxBus1.listen(RxEvent.ResponseOrderId::class.java).subscribe {
            adaptor.notifyDataSetChanged().apply {
                invoicelist[it.position].uniqueIdentifiaction = it.orderId
                invoicelist[it.position].paymentStatus = "Paid"

                if (invoicelist[it.position].paymentStatus == "Paid") {
                    view.findViewById<Button>(R.id.btn_details).visibility = View.INVISIBLE
                }
            }
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

            customerName = service.customerName
            customerid = service.customerid
            amount = service.amount
            paystatus = service.paymentStatus
            expandable = service.expandable
            invoiceno = service.invoiceNO
            paymentOrder = PaymentOrder(service.uniqueIdentifiaction,
                service.customerName,
                service.customerid,
                service.amount,
                service.paymentStatus,
                service.expandable,
                service.orderId,
                service.invoiceNO)

            alertmessageCustom(service.amount, position)
            // dialog.show()

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
//            val action = CustomerListDirections.actionCustomerListToSwipeCardPayment()
//            findNavController().navigate(action)
            mAlertDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()


        listenDisposable.dispose()
    }

    override fun getViewModel() = CustomerListViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = FragmentCustomerListBinding.inflate(inflater, container, false)

}





