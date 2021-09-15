package com.jcclover.jcel.customerdetail

import android.accounts.Account
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.Intents
import com.clover.sdk.v3.order.*
import com.jcclover.R
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.PaymentOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CustomerDetail : Fragment() {
    var customerName:TextView?=null
    var customerId:TextView?=null
    var amount:TextView?=null
    var orderId:TextView?=null
    var paymentStatus:TextView?=null
    lateinit var btn_pay:Button
    lateinit var mOrder: Order
 var position:Int?=0
    var mOrderConnector:OrderConnector?=null
    private var mAccount: Account? = null
    val SECURE_PAY_REQUEST_CODE=1
    private lateinit var paymentOrder:PaymentOrder
    private lateinit var orderID:String
    var cardEntryMethodAllowed=Intents.CARD_ENTRY_METHOD_MAG_STRIPE or  Intents.CARD_ENTRY_METHOD_ICC_CONTACT or Intents.CARD_ENTRY_METHOD_NFC_CONTACTLESS or Intents.CARD_ENTRY_METHOD_MANUAL
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getCloverAccount()
        connect()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         customerName=view.findViewById(R.id.cus_name)
         customerId=view.findViewById(R.id.cus_id)
         amount=view.findViewById(R.id.invoice_amount)
         paymentStatus=view.findViewById(R.id.paymentstatus_details)
         orderId=view.findViewById(R.id.order_id)
         btn_pay=view.findViewById(R.id.btn_pay) as Button


RxBus1.listen(RxEvent.CustomerDetail::class.java).subscribe(){

   getCustomerDetail(it)
}



        btn_pay.setOnClickListener {
            if (paymentStatus!!.text!="Paid"){
                btn_pay.visibility=View.VISIBLE
            GlobalScope.launch(Dispatchers.IO) {
                createOrder()
            }
        }else{
            btn_pay.visibility=View.GONE
        }
        }



    }

     fun createOrder() {

       mOrder=mOrderConnector!!.createOrder(Order())
        Log.d("msg","orderId${mOrder.id}")
         mOrder.setPayType(PayType.FULL)
         paymentOrder.uniqueIdentifiaction=mOrder.id
         orderID=mOrder.id
         addCustomLineItems(mOrder.id)
         cloverPay(mOrder.id,mOrder)

     }

    fun addCustomLineItems(mOrderId:String){
        var  mLineItem=LineItem().apply {
            note="generated "
            price=amount!!.text.toString().toLong()
        }

       mOrderConnector!!.addCustomLineItem(mOrder.id,mLineItem,false)
    }

    fun cloverPay(mOrderId:String,mOrder:Order){
        Log.d("msg","OrderId $mOrderId")
        val intent = Intent(Intents.ACTION_CLOVER_PAY)
        intent.putExtra(Intents.EXTRA_ORDER_ID,mOrderId)
        intent.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS,cardEntryMethodAllowed)
        startActivityForResult(intent, SECURE_PAY_REQUEST_CODE)

    }
    fun orderPaymentStatus(myOrder:Order){

        if (myOrder.payments[0].tender.label=="Cash"){
            Log.d("msg","new details cash")

when (myOrder.paymentState){
    PaymentState.PAID-> { Log.d("msg","new details Paid")
    myOrder.clearPayments()
        myOrder.clearState()
        Log.d("msg","${myOrder.state}")
    }
    PaymentState.OPEN-> {  Log.d("msg","new details Open")
        myOrder.clearPayments()}
    PaymentState.PARTIALLY_PAID->{ Log.d("msg","new details Partialiallyrefund")
        myOrder.clearPayments()}
}
        }else{
            Log.d("msg","new details card")
            Log.d("msg","new details card ${myOrder.paymentState}")
            when (myOrder.paymentState){
                PaymentState.PAID-> { Log.d("msg","new details Paid")
                    Log.d("msg","new details LOcked")
                   }
                PaymentState.OPEN-> {  Log.d("msg","new details Open")
                   }
                PaymentState.PARTIALLY_PAID->{ Log.d("msg","new details Partially_PAID")
                  }
            }
        }




    }
    private fun getCustomerDetail(it: RxEvent.CustomerDetail?) {
position=it?.position
        paymentOrder= PaymentOrder("",it?.selectedCustomerDetail?.customerName.toString(),it?.selectedCustomerDetail?.customerid.toString(),it?.selectedCustomerDetail?.amount.toString(),it?.selectedCustomerDetail?.paymentStatus.toString(),false)
        customerName?.text=it?.selectedCustomerDetail?.customerName
        customerId?.text=it?.selectedCustomerDetail?.customerid
        amount?.text=it?.selectedCustomerDetail?.amount
        paymentStatus?.text=it?.selectedCustomerDetail?.paymentStatus
        orderId?.text=it?.selectedCustomerDetail?.uniqueIdentifiaction
if (paymentStatus!!.text!="Paid"){
    btn_pay.visibility=View.VISIBLE
}

    }
    fun getCloverAccount() {
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(activity)
            if (mAccount == null) {
                return
            }
        }

    }
    private fun connect() {
        disconnect()
        if (mAccount != null) {
            mOrderConnector = OrderConnector(activity, mAccount, null)
            mOrderConnector!!.connect()
        }
    }
    private fun disconnect() {
        if (mOrderConnector!=null){
            mOrderConnector!!.disconnect()
            mOrderConnector = null
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SECURE_PAY_REQUEST_CODE  && resultCode  == Activity.RESULT_OK){
            Toast.makeText(activity, "RESULT_OK", Toast.LENGTH_SHORT).show()
            paymentStatus!!.text="Paid"
            paymentOrder.paymentStatus= paymentStatus!!.text.toString()
            paymentStatus!!.textColors.defaultColor
            orderId!!.text=orderID


GlobalScope.launch(Dispatchers.IO){
    Log.d("msg","OrderId1  ${orderID}")
    Log.d("msg","OrderId2  ${paymentOrder.uniqueIdentifiaction}")
    var myorder=mOrderConnector!!.getOrder(mOrder.id)
  orderPaymentStatus(myorder)
}

           paymentOrder= PaymentOrder(orderID,customerName!!.text.toString(),customerId!!.text.toString(),amount!!.text.toString(),paymentStatus!!.text.toString(),false)
          Log.d("msg","paymentOrder${paymentOrder}")
          RxBus1.publish(RxEvent.selecteddetails(paymentOrder,position!!))

        }else {
            Toast.makeText(activity, "RESULT_CANCELED", Toast.LENGTH_SHORT).show()
        }
    }

    }
