package com.jcclover.jcel.customerlist

import android.accounts.Account
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.order.*
import com.jcclover.R
import com.jcclover.jcel.Implementation.OnServiceClickListner
import com.jcclover.jcel.customercarddetails.SwipeCardPayment
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.repository.Repository
import com.jcclover.jcel.util.AlertDialogue
import com.jcclover.jcel.util.log
import com.jcclover.jcel.util.toast


class CustomerList : Fragment(),OnServiceClickListner {
    private lateinit var dialog: Dialog
    private var mAccount: Account? = null
    private lateinit var rcCusList: RecyclerView
    var mOrderConnector: OrderConnector? = null
    private lateinit var paymentOrder: PaymentOrder
    lateinit var adaptor: CustomerListAdaptor
    var customerName: String? = null
    var customerid: String? = null
    var amount: String? = null
    var paystatus: String? = null
    var expandable: Boolean = false
    var invoiceno: String? = null
    private lateinit var viewModel: MainViewModel

    companion object {
        var invoicelist = InvoiceList().createList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        activity?.title = "Invoice List";
        getCloverAccount()
        connect()
        //to remove the back button in top of the fragment
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        rcCusList = view.findViewById(R.id.customerlist_recycler_view)
        recyclerView()

        RxBus1.listen(RxEvent.ResponseOrderId::class.java).subscribe {


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
        adaptor = CustomerListAdaptor(Companion.invoicelist)
        rcCusList.layoutManager = LinearLayoutManager(activity)
        adaptor.setonClickListner(this)
        rcCusList.adapter = adaptor

    }

    private fun getCloverAccount() {
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(activity)
            if (mAccount == null) {
                return
            }
        }
    }

    override fun onDestroy() {
        super<Fragment>.onDestroy()
        disconnect()
    }

    private fun connect() {
        disconnect()
        if (mAccount != null) {
            mOrderConnector = OrderConnector(activity, mAccount, null)
            mOrderConnector!!.connect()
        }
    }

    private fun disconnect() {
        if (mOrderConnector != null) {
            mOrderConnector!!.disconnect()
            mOrderConnector = null
        }
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

            alertmessage(service.amount, position)
            dialog.show()

        }

    }

    fun alertmessage(amount: String, position: Int) {
        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.customdialog)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.window?.setBackgroundDrawable(requireActivity().getDrawable(R.drawable.background))
        }
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
         dialog.window?.attributes?.windowAnimations=R.style.DialogAnimation
        val swipe: Button = dialog.findViewById(R.id.btn_cancel)
        val manual: Button = dialog.findViewById(R.id.btn_okay)
        manual.setOnClickListener {
            requireActivity().toast("Manual card Entry")
            val action =
                CustomerListDirections.actionCustomerListToCustomerCardDetails(amount.toInt(),
                    position)
            findNavController().navigate(action)
            dialog.dismiss()

        }
        swipe.setOnClickListener {
            requireActivity().toast("Swipe card")
            val action =
                CustomerListDirections.actionCustomerListToSwipeCardPayment()
            findNavController().navigate(action)

            dialog.dismiss()
        }


    }
}






