package com.jcclover.jcel.customerlist

import android.accounts.Account
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.Intents
import com.clover.sdk.v3.order.*
import com.jcclover.R
import com.jcclover.jcel.Implementation.OnServiceClickListner
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.InvoiceDeatils
import com.jcclover.jcel.modelclass.PaymentOrder
import com.jcclover.jcel.modelclass.SelectedCustomerDetail
import kotlinx.coroutines.*


class CustomerList : Fragment(),OnServiceClickListner {
    private var mAccount: Account? = null
    private lateinit var rcCusList : RecyclerView
    lateinit var mOrder: Order
    lateinit var myorder: Order
    var position:Int?=0
    var mOrderConnector: OrderConnector?=null
    val SECURE_PAY_REQUEST_CODE=1
    private lateinit var paymentOrder:PaymentOrder
    private lateinit var orderID:String
    var cardEntryMethodAllowed=
        Intents.CARD_ENTRY_METHOD_MAG_STRIPE or  Intents.CARD_ENTRY_METHOD_ICC_CONTACT or Intents.CARD_ENTRY_METHOD_NFC_CONTACTLESS or Intents.CARD_ENTRY_METHOD_MANUAL
    var paymentdetailsList = ArrayList<PaymentOrder>()
    lateinit  var adaptor:CustomerListAdaptor
    var customerName:String?=null
    var customerid:String?=null
   var  amount:String?=null
    var paystatus:String?=null
   var  expandable:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getCloverAccount()
        connect()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rcCusList = view.findViewById(R.id.customerlist_recycler_view)
        recyclerView()
        RxBus1.listen(RxEvent.CustomerDetail::class.java).subscribe(){
            Log.d("mag","orderId 1${it?.selectedCustomerDetail?.orderId}")
            var idnew=it?.selectedCustomerDetail?.orderId
            var position=it?.position
            paymentdetailsList[position!!].orderId=idnew.toString()
            paymentdetailsList[position!!].uniqueIdentifiaction=idnew.toString()
            adaptor.notifyDataSetChanged()
        }

        RxBus1.listen(RxEvent.selecteddetails::class.java).subscribe(){
           var orderId=it?.seletected?.uniqueIdentifiaction
            var paymentstatus=it?.seletected?.paymentStatus
            var position=it?.position
            paymentdetailsList[position!!].amount=it?.seletected?.amount.toString()
            paymentdetailsList[position!!].uniqueIdentifiaction=orderId.toString()
            paymentdetailsList[position!!].paymentStatus=paymentstatus.toString()
            paymentdetailsList[position!!].orderId=orderId.toString()
            adaptor.notifyDataSetChanged()
        }
    }
    private fun recyclerView() {
        adaptor=CustomerListAdaptor(createList())
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
        super.onDestroy()
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
    private fun createList() : ArrayList<PaymentOrder> {
        paymentdetailsList = ArrayList<PaymentOrder>()
        paymentdetailsList.add(PaymentOrder("01","Jai","100","450","notCreated",false,""))
        paymentdetailsList.add(PaymentOrder("02","Chandran","101","625","notCreated",false,""))
        paymentdetailsList.add(PaymentOrder("03","Ravi","102","1235","notCreated",false,""))
        paymentdetailsList.add(PaymentOrder("04","Carclo","103","450","notCreated",false,""))
        paymentdetailsList.add(PaymentOrder("05","MUTLILINk","104","225","notCreated",false,""))
        paymentdetailsList.add(PaymentOrder("06","Carspa","105","125","notCreated",false,""))

        return paymentdetailsList
    }

    override fun onServiceClicked(service: Any,position:Int) {
      if (service is PaymentOrder){
           this.position=position
            Toast.makeText(activity, "Invoice number  ${service.customerid}", Toast.LENGTH_SHORT).show()
             customerName=service.customerName
             customerid=service.customerid
             amount=service.amount
             paystatus=service.paymentStatus
             expandable=service.expandable
           paymentOrder= PaymentOrder(service.uniqueIdentifiaction,service.customerName,service.customerid,service.amount,service.paymentStatus,service.expandable,service.orderId)

          GlobalScope.launch(Dispatchers.Main) {
       var detail= lunchBackgroundThread(paymentOrder)
            settingOrderId(detail)
          }
}


    }

    private fun settingOrderId(detail: PaymentOrder) {

        RxBus1.publish(RxEvent.CustomerDetail(detail,position!!))
    }

    suspend fun lunchBackgroundThread(paymentOrder: PaymentOrder):PaymentOrder {
        return withContext(Dispatchers.IO){
            var invoiceData= createOrder(paymentOrder)

            return@withContext invoiceData
        }

    }

    suspend  fun createOrder(paymentOrder: PaymentOrder):PaymentOrder{

if (paymentOrder.orderId.isNullOrEmpty()) {
    mOrder = mOrderConnector!!.createOrder(Order())
    mOrder.setPayType(PayType.FULL)
    paymentOrder.orderId = mOrder.id
  orderID = mOrder.id
    addCustomLineItems(mOrder.id)

}else{
    mOrderConnector!!.getOrder(paymentOrder.orderId)
}
      cloverPay(paymentOrder.orderId)
return paymentOrder
    }
    fun addCustomLineItems(mOrderId:String){
        var  mLineItem= LineItem().apply {
            note="generated "
            price= paymentOrder.amount.toString().toLong()
        }

        mOrderConnector!!.addCustomLineItem(mOrderId,mLineItem,false)
    }
    fun cloverPay(mOrderId:String){
        Log.d("msg","OrderId $mOrderId")
        val intent = Intent(Intents.ACTION_CLOVER_PAY)
        intent.putExtra(Intents.EXTRA_ORDER_ID,mOrderId)
        intent.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS,cardEntryMethodAllowed)
       // startActivityForResult(intent, SECURE_PAY_REQUEST_CODE,)
resultLauncher.launch(intent)
    }
var resultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

    if ( result.resultCode  == Activity.RESULT_OK){
        Toast.makeText(activity, "RESULT_OK", Toast.LENGTH_SHORT).show()

        //   paymentOrder.paymentStatus="Paid"
        paymentOrder.uniqueIdentifiaction=orderID

  GlobalScope.launch(Dispatchers.Main) {
      var getOrderdetails=getOrderDetails()
updateStatus(getOrderdetails)
        }
    }else {
        Toast.makeText(activity, "RESULT_CANCELED", Toast.LENGTH_SHORT).show()
    }
}

    private fun updateStatus(orderdetails: InvoiceDeatils?) {
        Toast.makeText(activity, "${orderdetails!!.message}", Toast.LENGTH_SHORT).show()
        paymentOrder= PaymentOrder(orderID,customerName!!,customerid!!,orderdetails!!.amount,orderdetails!!.paymentStatus,false,orderID)
        Log.d("msg","paymentOrder${paymentOrder}")
        RxBus1.publish(RxEvent.selecteddetails(paymentOrder,position!!))

    }

    suspend  fun getOrderDetails(): InvoiceDeatils? {
    return withContext(Dispatchers.IO) {
    myorder= mOrderConnector!!.getOrder(paymentOrder.orderId)
   var invoice =orderPaymentStatus(myorder)
        Log.d("msg", "new details getOrderDetails ")
    return@withContext invoice
}
}

    fun orderPaymentStatus(myOrder:Order): InvoiceDeatils? {

        var invoiceDetails: InvoiceDeatils? = null
        if (myOrder.payments[0].tender.label == "Cash") {
            myOrder.clearPayments()
        }
        else {
            Log.d("msg", "new details card ${amount}")
            Log.d("msg", "new details card ${myOrder.payments[0].amount}")

            var payableamount = myOrder.payments[0].amount.toString()
            if (payableamount == amount) {
                Log.d("msg", "new details card paid")
                invoiceDetails = InvoiceDeatils(payableamount, "Paid","Payment Successfully done")

            } else {
                var amountref = 0L
                for (i in 0 until myOrder.payments.size - 1) {
                    amountref = amountref + myOrder.payments[i].amount

                }

                if (amount!!.toLong()== amountref) {
                    invoiceDetails = InvoiceDeatils("0", "Paid", "Payment Successfully done")

                } else {
                    var pendingAmount =amount!!.toLong() - amountref
                    invoiceDetails = InvoiceDeatils(
                        pendingAmount.toString(),
                        "Partially_Paid",
                        "Partial payment Successfully done"
                    )

                    //toast Partial payment Successfully done
                }

                amountref = 0
            }
        }



return invoiceDetails
    }

    }
