package com.jcclover.jcel.repository

import com.google.gson.Gson
import com.jcclover.jcel.network.ApiResponse

import com.jcclover.jcel.modelclass.*
import com.jcclover.jcel.network.ApiInstances
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class Repository {


    suspend fun createCharge(charge:Charges):Response<OrderDetails>{
        return ApiInstances.api2.createCharge(charge)
    }

    suspend fun paymentToken(apikey:String,card: CardInfo): ApiResponse<TokenResponse> {
        return try {
            val response=ApiInstances.api.paymentToken(card, apikey)
            ApiResponse.Success(response)
        }catch (e:HttpException){
           var errormessage= errorMessagefromapi(e)
         ApiResponse.CustomError(errormessage!!)
        }

    }

        private fun errorMessagefromapi(httpException: HttpException): String? {
            var errorMessage: String? = null
            val error = httpException.response()?.errorBody()
            try {
                val adapter = Gson().getAdapter(ErrorResponse::class.java)
                val errorParser = adapter.fromJson(error?.string())
                errorMessage = errorParser.error.message
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                return errorMessage
            }
        }


}