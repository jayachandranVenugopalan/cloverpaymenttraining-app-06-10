package com.jcclover.jcel.network

import com.jcclover.jcel.modelclass.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

//RestApi 2
interface ApiStories {



    @POST("v1/tokens")
    suspend fun paymentToken(
        @Body card: CardInfo,
        @Header("apikey") apikey:String
    ):TokenResponse

    @Headers("Accept:application/json",
        "Authorization:Bearer 21b142cd-2562-e94e-2c72-860c74d56b37")
      @POST("v1/charges")
    suspend fun createCharge(
        @Body charge:Charges
    ):Response<OrderDetails>
}