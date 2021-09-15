package com.jcclover.jcel.customerlist

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.inventory.InventoryConnector
import com.jcclover.R
import com.jcclover.jcel.Implementation.OnServiceClickListner
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.PaymentOrder


class CustomerList : Fragment(),OnServiceClickListner {
    private var mAccount: Account? = null
    private var mInventoryConnector: InventoryConnector? = null
    private lateinit var rcCusList : RecyclerView
   // var paymentdetailsList=ArrayList<PaymentOrder>()
    var paymentdetailsList = ArrayList<PaymentOrder>()
    lateinit  var adaptor:CustomerListAdaptor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getCloverAccount()
        connect()

        rcCusList = view.findViewById(R.id.customerlist_recycler_view)
        recyclerView()
        RxBus1.listen(RxEvent.selecteddetails::class.java).subscribe(){
           var orderId=it?.seletected?.uniqueIdentifiaction
            var paymentstatus=it?.seletected?.paymentStatus
            var position=it?.position
            paymentdetailsList[position!!].uniqueIdentifiaction=orderId.toString()
            paymentdetailsList[position!!].paymentStatus=paymentstatus.toString()
            adaptor.notifyDataSetChanged()
        }
    }

    fun getCloverAccount(){
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(activity)
            if (mAccount == null) {
                return
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        disconnect()
    }
    private fun connect() {
        disconnect()
        if (mAccount != null) {
            mInventoryConnector = InventoryConnector(activity, mAccount, null)
            mInventoryConnector!!.connect()
        }
    }
    private fun disconnect() {
        if (mInventoryConnector != null) {
            mInventoryConnector!!.disconnect()
            mInventoryConnector = null
        }
    }
    private fun createList() : ArrayList<PaymentOrder> {
        //ArrayList<Any> is for both headers and items

        paymentdetailsList = ArrayList<PaymentOrder>()
        paymentdetailsList.add(PaymentOrder("01","jai","100","450","notCreated",false))
        paymentdetailsList.add(PaymentOrder("02","chandran","101","625","notCreated",false))

        paymentdetailsList.add(PaymentOrder("03","ravi","102","1235","notCreated",false))
        paymentdetailsList.add(PaymentOrder("04","carclo","103","450","notCreated",false))
        paymentdetailsList.add(PaymentOrder("05","MUTLILINk","104","225","notCreated",false))
        paymentdetailsList.add(PaymentOrder("06","Carspa","105","125","notCreated",false))

        return paymentdetailsList
    }
    private fun recyclerView() {
adaptor=CustomerListAdaptor(createList())
        rcCusList.layoutManager = LinearLayoutManager(activity)
        adaptor.setonClickListner(this)

        rcCusList.adapter = adaptor

//        rcCusList?.setHasFixedSize(true)

    }


    override fun onServiceClicked(service: Any,position:Int) {
if (service is PaymentOrder){
    Log.d("msg","in main fragment")


        Toast.makeText(activity, "new name" +
                "${service.customerName}", Toast.LENGTH_SHORT).show()
var cusDetail:PaymentOrder= PaymentOrder(service.uniqueIdentifiaction,service.customerName,service.customerid,service.amount,service.paymentStatus,service.expandable)
Log.d("msg","${cusDetail.customerName}")
    RxBus1.publish(RxEvent.CustomerDetail(cusDetail,position))


    }

    }
}