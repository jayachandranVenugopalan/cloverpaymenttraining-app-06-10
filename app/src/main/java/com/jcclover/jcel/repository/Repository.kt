package com.jcclover.jcel.repository

import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.network.ApiInstances
import retrofit2.Response

class Repository {

//    suspend fun getPost():Response<Merchent>{
//        return ApiInstances.api.getPost()
//    }
    suspend fun getPost1(id:String):Response<Merchent>{
        return ApiInstances.api.getPost1(id)
    }
    suspend fun createCustomerDetails(cards: cards):Response<CreateCustomerResponse>{
        return ApiInstances.api.createCustomerDetails(cards)
    }

    suspend fun tokenPay(number:String,exp_month:String,exp_year:String,cvv:String):Response<TokenResponse>{
        return ApiInstances.api.tokenPay(number, exp_month, exp_year, cvv)
    }

    suspend fun createPayment(mid:String):Response<PaymentResponse>{
        return ApiInstances.api.createPayment(mid)
    }

    suspend fun createCharge(charge:Charges):Response<OrderDetails>{
        return ApiInstances.api2.createCharge(charge)
    }

    suspend fun paymentToken(apikey:String,card: CardInfo):Response<TokenResponse>{
        return ApiInstances.api.paymentToken(card, apikey)
    }
//    suspend fun getPostdemo():Response<Post>{
//        return ApiInstances.api.getpostdemo()
//    }
//    suspend fun setPost( post: Post):Response<Post>{
//        return ApiInstances.api.setPost(post)
//    }
//    suspend fun  getAllOrder():Response<GetOrders>{
//        return ApiInstances.api.getAllOrder()
//    }
}