package com.jcclover.jcel.network

sealed class ApiResponse<out T> {
    data class Success<T>(var response : T) : ApiResponse<T>()
    data class Error(var exception : Exception) : ApiResponse<Nothing>()
    data class CustomError(var message: String) : ApiResponse<Nothing>()

}