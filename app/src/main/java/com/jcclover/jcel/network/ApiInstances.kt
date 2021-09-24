package com.jcclover.jcel.network

import com.jcclover.jcel.util.Constant.Companion.BASE_URL
import com.jcclover.jcel.util.Constant.Companion.BASE_URLAPIKEY

import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//RestApi 4
object ApiInstances  {
//
//    private val client= OkHttpClient.Builder().apply {
//        addInterceptor(MyInterceptor())
//    }.build()

    private val retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URLAPIKEY)
         //   .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api:ApiStories by lazy {
        retrofit.create(ApiStories::class.java)
    }
}