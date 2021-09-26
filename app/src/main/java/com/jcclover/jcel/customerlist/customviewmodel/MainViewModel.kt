package com.jcclover.jcel.customerlist.customviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clover.sdk.v1.customer.Card
import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(var repository: Repository): ViewModel() {

    val myReponse:MutableLiveData<Response<Merchent>> = MutableLiveData()
val myCreatrCustomerResponse:MutableLiveData<Response<CreateCustomerResponse>> = MutableLiveData()
    val myOwnidResponse:MutableLiveData<Response<TokenResponse>> =MutableLiveData()
val myCreatePayment:MutableLiveData<Response<PaymentResponse>> = MutableLiveData()
 val myReponse1:MutableLiveData<Response<Post>> = MutableLiveData()
    val myPaymentToken:MutableLiveData<Response<TokenResponse>> = MutableLiveData()
    val mycreateCHangeResponsw:MutableLiveData<Response<OrderDetails>> = MutableLiveData()
//
//    val myResponseGetOrder:MutableLiveData<Response<GetOrders>> = MutableLiveData()

//    fun getPost() {
//        viewModelScope.launch {
//            val response =  repository.getPost()
//             myReponse.value=response
//        }
//    }


    fun getPost1(id:String) {
        viewModelScope.launch {
            val response =  repository.getPost1(id)
            myReponse.value=response
        }
    }

    fun createCustomerDetails(cards: cards){
        viewModelScope.launch {
            val response=  repository.createCustomerDetails(cards)
            myCreatrCustomerResponse.value=response
        }
    }

    fun tokenPAy(number:String,exp_month:String,exp_year:String,cvv:String){
        viewModelScope.launch {
            val response= repository.tokenPay(number, exp_month, exp_year, cvv)
            myOwnidResponse.value=response
        }
    }

    fun createPayment(mid:String){
        viewModelScope.launch {
            val response=repository.createPayment(mid)
            myCreatePayment.value=response
        }
    }
    fun createChange(charge:Charges){
        viewModelScope.launch {
            val response=repository.createCharge(charge)
            mycreateCHangeResponsw.value=response
        }
    }

    fun paymentToken(apikey:String,card: CardInfo){
        viewModelScope.launch {
            val response=repository.paymentToken(apikey,card)
            myPaymentToken.value=response
        }
    }
    fun launchBackgroudThread(){
        GlobalScope.launch(Dispatchers.Main) {

        }
    }

//    fun getPostdemo() {
//        viewModelScope.launch {
//            val response =  repository.getPostdemo()
//            myReponse1.value=response
//        }
//    }
//
//    fun setPost( post: Post) {
//        viewModelScope.launch {
//            val response =  repository.setPost(post)
//            myReponse1.value=response
//        }
//    }
//
//    fun getAllOrder(){
//        viewModelScope.launch {
//            val response = repository.getAllOrder()
//            myResponseGetOrder.value= response
//        }
//    }

}