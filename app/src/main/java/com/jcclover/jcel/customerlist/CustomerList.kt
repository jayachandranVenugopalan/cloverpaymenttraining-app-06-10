package com.jcclover.jcel.customerlist

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.order.*
import com.jcclover.R
import com.jcclover.jcel.Implementation.OnServiceClickListner
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.repository.Repository


class CustomerList : Fragment(),OnServiceClickListner {

    private var mAccount: Account? = null
    private lateinit var rcCusList : RecyclerView
     var mOrderConnector: OrderConnector?=null
    private lateinit var paymentOrder:PaymentOrder
    lateinit  var adaptor:CustomerListAdaptor
    var customerName:String?=null
    var customerid:String?=null
   var  amount:String?=null
    var paystatus:String?=null
   var  expandable:Boolean=false
    var invoiceno:String?=null
    private lateinit var  viewModel:MainViewModel

companion object{
    var invoicelist=InvoiceList().createList()
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        getCloverAccount()
        connect()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val repository=Repository()
        val viewModelFactory=MainViewModelFactory(repository)
        viewModel=ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
        rcCusList = view.findViewById(R.id.customerlist_recycler_view)
        recyclerView()

        RxBus1.listen(RxEvent.ResponseOrderId::class.java).subscribe{
    Log.d("chargeId 123","${it.orderId}")
    Log.d("chargeId 123","${it.position}")

    adaptor.notifyDataSetChanged().apply { invoicelist[it.position].uniqueIdentifiaction=it.orderId
        invoicelist[it.position].paymentStatus="Paid"

        if( invoicelist[it.position].paymentStatus=="Paid"){
            view.findViewById<Button>(R.id.btn_details).visibility=View.INVISIBLE
        }
    }
}





    }
    private fun recyclerView() {
        adaptor=CustomerListAdaptor(Companion.invoicelist)
        rcCusList.layoutManager = LinearLayoutManager(activity)
        adaptor.setonClickListner(this)
        rcCusList.adapter = adaptor

    }
    private fun getCloverAccount(){
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
            mOrderConnector= OrderConnector(activity, mAccount, null)
            mOrderConnector!!.connect()
        }
    }
    private fun disconnect() {
        if (mOrderConnector != null) {
            mOrderConnector!!.disconnect()
            mOrderConnector = null
        }
    }

    override fun onServiceClicked(service: Any,position:Int) {
      if (service is PaymentOrder){

             customerName=service.customerName
             customerid=service.customerid
             amount=service.amount
             paystatus=service.paymentStatus
             expandable=service.expandable
          invoiceno=service.invoiceNO

           paymentOrder= PaymentOrder(service.uniqueIdentifiaction,service.customerName,service.customerid,service.amount,service.paymentStatus,service.expandable,service.orderId,service.invoiceNO)
          Log.d("postion pos ","postion pos$position")
          Log.d("postion orderId ","postion orderId${service.orderId}")
          Log.d("postion amount ","postion amount${service.amount}")



val action=CustomerListDirections.actionCustomerListToCustomerCardDetails(service.amount.toInt(),position)
          findNavController().navigate(action)

}}}






