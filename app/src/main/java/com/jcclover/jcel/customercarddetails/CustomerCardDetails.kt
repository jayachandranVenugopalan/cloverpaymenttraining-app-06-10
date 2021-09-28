package com.jcclover.jcel.customercarddetails

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jcclover.R
import com.jcclover.jcel.network.ApiResponse

import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.*

import com.jcclover.jcel.repository.Repository
import com.jcclover.jcel.util.Constant.Companion.APIKEY
import java.lang.IllegalArgumentException
import java.util.*


class CustomerCardDetails : Fragment() {
    private lateinit var  viewModel:MainViewModel
    var sucess=false
    var editor:SharedPreferences.Editor?=null
    var sharedpreferences: SharedPreferences? = null
var amount:Int?=0
     var position:Int?=0
    private  lateinit var paymentToken:String
    private  var chargeID:String?=null
    lateinit var processbar:ProgressBar
private val args:CustomerCardDetailsArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        sharedpreferences=requireActivity().getSharedPreferences("mylab", Context.MODE_PRIVATE)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        return inflater.inflate(com.jcclover.R.layout.fragment_customer_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        processbar=view.findViewById(R.id.progressbar) as ProgressBar
        var holderName:EditText=view.findViewById(R.id.firstname_et)
        var cardNumber:EditText=view.findViewById(R.id.cardnumber_et)
        var cvv:EditText=view.findViewById(R.id.cvv_et)
        var experiationMonth:EditText=view.findViewById(R.id.expmonth_et)
        var experiationYear:EditText=view.findViewById(R.id.expyear_et)
       val editor:SharedPreferences.Editor=sharedpreferences!!.edit()
        Log.d("postion","onviewCreatedcalled")
        processbar.visibility=View.GONE
                view.findViewById<Button>(R.id.save_btn).setOnClickListener {
                    validation(cardNumber,cvv,experiationMonth,experiationYear)
                }



    }

    private fun validation(cardNumber:EditText,cvv:EditText,experiationMonth:EditText,experiationYear:EditText){
        processbar.visibility=View.VISIBLE
        var year= Calendar.getInstance().get(Calendar.YEAR)
        var enteredYear="20"+experiationYear.text.toString()
        Log.d("postion","onviewCreatedcalled${"20"+experiationYear.text.toString()}")
        if(cardNumber.text.toString().length in 13..16){
            if(cvv.text.toString().length==4||cvv.text.toString().length==3){
                if (experiationMonth.text.toString().length == 2 && experiationMonth.text.toString().toInt()<=12 ){
                    if (experiationYear.text.toString().length == 2 && enteredYear != year.toString() &&  enteredYear >= year.toString()){
                        createPaymentToken(CardInfo(CardDetails(cvv.text.toString(),experiationMonth.text.toString(),experiationYear.text.toString(),cardNumber.text.toString())),args.amount,args.position)
                    }else{
                        processbar.visibility=View.GONE
                        experiationYear.error="Enter the valid experiation year"
                    }
                }else{
                    processbar.visibility=View.GONE
                    experiationMonth.error="Enter the valid experiation month"
                }
            }else{
                processbar.visibility=View.GONE
                cvv.error="Enter the valid cvv number"
            }
        }else{
            processbar.visibility=View.GONE
            cardNumber.error="Enter the valid card number"
        }

    }

    private fun cardValidation(cardNumber: String): Boolean {
        val creditCard: String = cardNumber.trim()
        var applyAlgo = false
        when (cardNumber) {
            cardNumber ->
                // VISA credit cards has length 13 - 15
                // VISA credit cards starts with prefix 4
                if (creditCard.length >= 13 && creditCard.length <= 16 && creditCard.startsWith("4")) {
                    applyAlgo = true
                }
            cardNumber ->             // MASTERCARD has length 16
                // MASTER card starts with 51, 52, 53, 54 or 55
                if (creditCard.length == 16) {
                    val prefix = creditCard.substring(0, 2).toInt()
                    if (prefix >= 51 && prefix <= 55) {
                        applyAlgo = true
                    }
                }
            cardNumber ->             // AMEX has length 15
                // AMEX has prefix 34 or 37
                if (creditCard.length == 15
                    && (creditCard.startsWith("34") || creditCard
                        .startsWith("37"))
                ) {
                    applyAlgo = true
                }
            cardNumber ->             // DINERSCLUB or CARTEBLANCHE has length 14
                // DINERSCLUB or CARTEBLANCHE has prefix 300, 301, 302, 303, 304,
                // 305 36 or 38
                if (creditCard.length == 14) {
                    val prefix = creditCard.substring(0, 3).toInt()
                    if (prefix >= 300 && prefix <= 305
                        || creditCard.startsWith("36")
                        || creditCard.startsWith("38")
                    ) {
                        applyAlgo = true
                    }
                }
            cardNumber ->             // DISCOVER card has length of 16
                // DISCOVER card starts with 6011
                if (creditCard.length == 16 && creditCard.startsWith("6011")) {
                    applyAlgo = true
                }
            cardNumber ->             // ENROUTE card has length of 16
                // ENROUTE card starts with 2014 or 2149
                if (creditCard.length == 16
                    && (creditCard.startsWith("2014") || creditCard
                        .startsWith("2149"))
                ) {
                    applyAlgo = true
                }
            cardNumber ->             // JCB card has length of 16 or 15
                // JCB card with length 16 starts with 3
                // JCB card with length 15 starts with 2131 or 1800
                if (creditCard.length == 16 && creditCard.startsWith("3")
                    || creditCard.length == 15 && (creditCard
                        .startsWith("2131") || creditCard
                        .startsWith("1800"))
                ) {
                    applyAlgo = true
                }
            else -> throw IllegalArgumentException()
        }
         if (applyAlgo) {
            return cardValidation (creditCard)
        }
        return false

    }


    private fun createPaymentToken(card:CardInfo,amount:Int,position:Int) {

        Log.d("postion2","$position")
        viewModel.paymentToken(APIKEY, card)
        viewModel.myPaymentToken.observe(requireActivity(), Observer { ApiResponse->

            when(ApiResponse){
                is ApiResponse.Success->{
                    Log.d("paymentToken",ApiResponse.response.id.toString())

                    paymentToken=ApiResponse.response.id
                    createChanges(paymentToken,amount,position)
                }
                is ApiResponse.CustomError->{
                    processbar.visibility=View.GONE
                    Toast.makeText(activity, "${ApiResponse.message}}", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun createChanges(token:String, amount:Int, position:Int) {
        Log.d("changes",token)
        viewModel.createChange(Charges("ecom",amount,"usd",token))
        viewModel.mycreateCHangeResponsw.observe(requireActivity(), Observer { response->
            if(response.isSuccessful){
                Log.d("changes","${response.body().toString()}")
                chargeID=response.body()?.id.toString()
               Log.d("postion1",position.toString())
                    RxBus1.publish(RxEvent.ResponseOrderId(chargeID!!,amount.toString(),position))
                    val action=CustomerCardDetailsDirections.actionCustomerCardDetailsToCustomerList()
                    findNavController().navigate(action)
                processbar.visibility=View.GONE


            }else{
                processbar.visibility=View.GONE
                Toast.makeText(activity, "${response.body()?.toString()}", Toast.LENGTH_SHORT).show()
                Log.d("changesfailure","${response.body().toString()}")
            }
        })
    }


}