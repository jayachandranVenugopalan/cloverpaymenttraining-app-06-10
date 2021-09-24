package com.jcclover.jcel.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var request:Request=chain.request()
            .newBuilder()
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization:","168415368431")
            .build()
        return chain.proceed(request)
    }
}