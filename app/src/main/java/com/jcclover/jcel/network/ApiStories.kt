package com.jcclover.jcel.network

import com.jcclover.jcel.modelclass.*
import retrofit2.Response
import retrofit2.http.*

//RestApi 2
interface ApiStories {

//    @Headers("Accept:application/json",
//             "Authorization:Bearer 21b142cd-2562-e94e-2c72-860c74d56b37")
//    @GET("v3/merchants/83H8Q583NRCK1")
//    suspend fun getPost():Response<Merchent>

    @Headers("Accept:application/json",
        "Authorization:Bearer 21b142cd-2562-e94e-2c72-860c74d56b37")
    @GET("v3/merchants/{postnumber}")
    suspend fun getPost1(
        @Path ("postnumber")postnumber:String
    ):Response<Merchent>


    @Headers("Accept:application/json",
        "Authorization:Bearer 21b142cd-2562-e94e-2c72-860c74d56b37")
    @GET("v3/merchants/{mID}/gateway")
    suspend fun getOwnID(
        @Path("mID") mId:String
    ):Response<OwnId>

    @Headers("Accept:application/json",
        "Authorization:Bearer 21b142cd-2562-e94e-2c72-860c74d56b37")
    @POST("v3/merchants/83H8Q583NRCK1/customers")
    suspend fun createCustomerDetails(
        @Body cards: cards
    ):Response<CreateCustomerResponse>

    @Headers("Accept:application/json",
        "Authorization:Bearer 21b142cd-2562-e94e-2c72-860c74d56b37")
     @POST("v3/merchants/{mId}/authorizations")
     suspend fun createPayment(
    @Path ("mId")mid:String
      ):Response<PaymentResponse>

    @Headers("Accept:application/json",
        "Authorization:Bearer 21b142cd-2562-e94e-2c72-860c74d56b37")
@POST("pakms/apikey")
    suspend fun PakmsApiKEY():Response<Post>


//
//    @GET("posts/1")
//    suspend fun getpostdemo():Response<Post>
//
//
//    @POST("posts")
//    suspend fun setPost(
//        @Body post: Post
//    ):Response<Post>
//
//
//    @Headers("Accept:application/json",
//        "Authorization:Bearer 21b142cd-2562-e94e-2c72-860c74d56b37")
//    @GET("v1/orders")
//    suspend fun getAllOrder():Response<GetOrders>
}