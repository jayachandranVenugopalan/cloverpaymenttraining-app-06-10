package com.jcclover.jcel.customerlist

import android.accounts.Account
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.Intents
import com.clover.sdk.v1.Intents.KIOSK_MODE_CARD_ENTRY_MASK_MANUAL
import com.clover.sdk.v3.order.*
import com.clover.sdk.v3.payments.Credit
import com.clover.sdk.v3.payments.Transaction
import com.clover.sdk.v3.payments.Type
import com.jcclover.R
import com.jcclover.jcel.Implementation.OnServiceClickListner
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.repository.Repository
import com.jcclover.jcel.util.Constant.Companion.CARDTYPE
import com.jcclover.jcel.util.Constant.Companion.CUSTOMERID
import com.jcclover.jcel.util.Constant.Companion.LAST4
import com.jcclover.jcel.util.Constant.Companion.MERCHANTID
import kotlinx.coroutines.*


class CustomerList : Fragment(),OnServiceClickListner {

    private var mAccount: Account? = null
    private lateinit var rcCusList : RecyclerView
    lateinit var mOrder: Order
    lateinit var myorder: Order
    var position:Int?=0
    var mOrderConnector: OrderConnector?=null
    private lateinit var paymentOrder:PaymentOrder
    private lateinit var orderID:String
    var cardEntryMethodAllowed= Intents.CARD_ENTRY_METHOD_MANUAL
    var paymentdetailsList = ArrayList<PaymentOrder>()
    lateinit  var adaptor:CustomerListAdaptor
    var customerName:String?=null
    var customerid:String?=null
   var  amount:String?=null
    var paystatus:String?=null
   var  expandable:Boolean=false
    var invoiceno:String?=null
    var sharedpreferences: SharedPreferences? = null
    private lateinit var  viewModel:MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedpreferences=requireActivity().getSharedPreferences("mylab", Context.MODE_PRIVATE)
        getCloverAccount()
        connect()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val repository=Repository()
        val viewModelFactory=MainViewModelFactory(repository)
        viewModel=ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)

        Log.d("SharedPreferences","CustomerID:-  ${sharedpreferences!!.getString(CUSTOMERID,"default")}")
        Log.d("SharedPreferences","Last4 Digit:- ${sharedpreferences!!.getString(LAST4,"0000")}")
        Log.d("SharedPreferences","CardType :-   ${sharedpreferences!!.getString(CARDTYPE,"UNKNOWN")}")
        rcCusList = view.findViewById(R.id.customerlist_recycler_view)
        recyclerView()
        RxBus1.listen(RxEvent.CustomerDetail::class.java).subscribe(){
            var idnew=it?.selectedCustomerDetail?.orderId
            var position=it?.position
            var statusopen="Open"
            paymentdetailsList[position!!].orderId=idnew.toString()
            paymentdetailsList[position!!].uniqueIdentifiaction=idnew.toString()
            paymentdetailsList[position!!].paymentStatus=statusopen
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

        viewModel.pakmsApiKey()
        viewModel.myReponse1.observe(requireActivity(), Observer {
            response->
            if (response.isSuccessful){
                Log.d("Apikey", response.raw().toString())
            }else{
                Log.d("Apikey failure",response.raw().toString())
            }
        })

      // getMerchentOrders()
getOwnID()
    }



    private fun getOwnID() {
        viewModel.getOwnID(MERCHANTID)
        viewModel.myOwnidResponse.observe(requireActivity(), Observer {
            response->
            if (response.isSuccessful){
                Log.d("Responsed is sucess", response.body().toString())
                Log.d("Responsed is sucess", response.body()?.mid.toString())
                Log.d("Responsed is sucess", response.headers().toString())
            }else{
                Log.d("Responsed is failure", response.errorBody().toString())
                Log.d("Responsed is failure", response.message().toString())
                Log.d("Responsed is failure", response.code().toString())
            }
        })

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
    private fun createList() : ArrayList<PaymentOrder> {
        paymentdetailsList = ArrayList<PaymentOrder>()
        paymentdetailsList.add(PaymentOrder("","Jai","100","450","",false,"","Jai5603"))
        paymentdetailsList.add(PaymentOrder("","Chandran","101","625","",false,"","Chandran5604"))
        paymentdetailsList.add(PaymentOrder("","Ravi","102","1235","",false,"","Ravi5605"))
        paymentdetailsList.add(PaymentOrder("","Carclo","103","450","",false,"","Carclo5606"))
        paymentdetailsList.add(PaymentOrder("","MUTLILINk","104","225","",false,"","MUTLILINk5607"))
        paymentdetailsList.add(PaymentOrder("","Carspa","105","125","",false,"","Carspa5608"))
        paymentdetailsList.add(PaymentOrder("","Metro","106","6000","",false,"","Metro5609"))
        paymentdetailsList.add(PaymentOrder("","Wriston","107","1500","",false,"","Wriston5610"))
        paymentdetailsList.add(PaymentOrder("","Elastomers","108","450","",false,"","Elastomers5611"))
        paymentdetailsList.add(PaymentOrder("","Mrp","109","245","",false,"","Mrp5612"))
        paymentdetailsList.add(PaymentOrder("","Rub","110","845","",false,"","Rub5613"))
        return paymentdetailsList
    }

    override fun onServiceClicked(service: Any,position:Int) {
      if (service is PaymentOrder){
           this.position=position
             customerName=service.customerName
             customerid=service.customerid
             amount=service.amount
             paystatus=service.paymentStatus
             expandable=service.expandable
          invoiceno=service.invoiceNO
           paymentOrder= PaymentOrder(service.uniqueIdentifiaction,service.customerName,service.customerid,service.amount,service.paymentStatus,service.expandable,service.orderId,service.invoiceNO)

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
    addCustomLineItems(paymentOrder)
}
else{

    mOrderConnector!!.getOrder(paymentOrder.orderId)
}
withContext(Dispatchers.Main){ cloverApiPay()}
      cloverPay(paymentOrder)
return paymentOrder
    }

   fun  cloverApiPay() {

viewModel.createPayment(MERCHANTID)
        viewModel.myCreatePayment.observe(requireActivity(), Observer { response->
            if (response.isSuccessful){
                Log.d("paymentResponed", response.body()?.message.toString())
            }else{
                Log.d("paymentResponed", response.raw().toString())
                Log.d("paymentResponefailure ", response.message().toString())
                Log.d("paymentResponefailure ", response.code().toString())
            }
        })


    }

    private  fun addCustomLineItems(paymentOrder:PaymentOrder){
        var  mLineItem= LineItem().apply {
            name="${paymentOrder.customerName} billable amount is \n  "
            note="\n ${paymentOrder.customerName}Groups.Pvt.Ltd  "
            price= paymentOrder.amount.toString().toLong()
        }

        mOrderConnector!!.addCustomLineItem(paymentOrder.orderId,mLineItem,false)
    }
    private  fun cloverPay(paymentOrder:PaymentOrder){
        val intent = Intent(Intents.ACTION_CLOVER_PAY)
        intent.putExtra(Intents.EXTRA_CUSTOMER_TENDER,Intents.CARD_ENTRY_METHOD_MANUAL)
        intent.putExtra(Intents.EXTRA_ORDER_ID,paymentOrder.orderId)
       // intent.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS,cardEntryMethodAllowed)
         resultLauncher.launch(intent)
    }

    var resultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

    if ( result.resultCode  == Activity.RESULT_OK){
        paymentOrder.uniqueIdentifiaction=orderID
  GlobalScope.launch(Dispatchers.Main) {
      var getOrderdetails=getOrderDetails()
updateStatus(getOrderdetails)
        }
    }else {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Payment is failed. Please try again")
        builder.setCancelable(false)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id -> })
        builder.create()
        builder.show()
    }
}

    private fun updateStatus(orderdetails: InvoiceDeatils?) {
        Toast.makeText(activity, "${orderdetails!!.message}", Toast.LENGTH_SHORT).show()
        paymentOrder= PaymentOrder(orderID,customerName!!,customerid!!,orderdetails!!.amount,orderdetails!!.paymentStatus,false,orderID,invoiceno!!)
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

    private  fun orderPaymentStatus(myOrder:Order): InvoiceDeatils? {

        var invoiceDetails: InvoiceDeatils? = null
        if (myOrder.payments[0].tender.label == "Cash") {
            myOrder.clearPayments()
        }
        else {
            var payableamount = myOrder.payments[0].amount.toString()
            if (payableamount == amount) {
                invoiceDetails = InvoiceDeatils(payableamount, "Paid","Payment Successfully done")
            } else {
                var amountref = 0L
                for (i in 0 until myOrder.payments.size - 1) {
                    amountref = amountref + myOrder.payments[i].amount

                }

                if (amount!!.toLong()== amountref) {
                    invoiceDetails = InvoiceDeatils("0", "Paid", "Payment Successfully done")

                } else if (amount!!.toLong()!= amountref){
                    var pendingAmount =amount!!.toLong() - amountref
                    invoiceDetails = InvoiceDeatils(
                        pendingAmount.toString(),
                        "Partially_Paid",
                        "Partial payment Successfully done"
                    )


                }
                else{
                    invoiceDetails = InvoiceDeatils("0", "Paid", "Payment Successfully done")

                }

                amountref = 0
            }
        }



return invoiceDetails
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedpreferences!!.edit().clear().apply()
            }


    }
