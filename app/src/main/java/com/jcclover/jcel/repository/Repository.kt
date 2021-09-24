package com.jcclover.jcel.repository

import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.network.ApiInstances
import retrofit2.Response
import retrofit2.http.POST

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

    suspend fun getOwnID(mid:String):Response<OwnId>{
        return ApiInstances.api.getOwnID(mid)
    }

    suspend fun createPayment(mid:String):Response<PaymentResponse>{
        return ApiInstances.api.createPayment(mid)
    }

    suspend fun pakmsApiKey():Response<Post>{
        return ApiInstances.api.PakmsApiKEY()
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