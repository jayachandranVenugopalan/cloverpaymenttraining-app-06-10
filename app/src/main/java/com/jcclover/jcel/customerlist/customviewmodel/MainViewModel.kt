package com.jcclover.jcel.customerlist.customviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcclover.jcel.network.ApiResponse
import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(var repository: Repository): ViewModel() {


    val myPaymentToken:MutableLiveData<ApiResponse<TokenResponse>> = MutableLiveData()
    val mycreateCHangeResponsw:MutableLiveData<Response<OrderDetails>> = MutableLiveData()

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


}