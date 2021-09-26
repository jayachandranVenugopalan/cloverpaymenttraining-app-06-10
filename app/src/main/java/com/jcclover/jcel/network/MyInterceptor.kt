package com.jcclover.jcel.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var request:Request=chain.request()
            .newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("apikey", "9600840a1133c8100f76543237f48ef8")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}