package com.jcclover.jcel.network

import com.jcclover.jcel.util.Constant.Companion.BASE_URL
import com.jcclover.jcel.util.Constant.Companion.BASE_URLAPIKEY
import com.jcclover.jcel.util.Constant.Companion.BASE_URl_ORDER
import com.jcclover.jcel.util.Constant.Companion.PAYMENTTOKEN_URL

import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//RestApi 4
object ApiInstances  {

    private val client= OkHttpClient.Builder().apply {
        addInterceptor(MyInterceptor())
    }.build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(PAYMENTTOKEN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val paymentApiRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URl_ORDER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api:ApiStories by lazy {
        retrofit.create(ApiStories::class.java)

    }
    val api2:ApiStories by lazy {
        paymentApiRetrofit.create(ApiStories::class.java)
    }
}