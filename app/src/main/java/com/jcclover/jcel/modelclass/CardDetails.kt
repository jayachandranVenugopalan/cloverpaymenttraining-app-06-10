package com.jcclover.jcel.modelclass

import com.clover.sdk.v1.customer.Card

data class CardDetails(
    val cvv: String,
    val exp_month: String,
    val exp_year: String,
    val number: String

)
data class CardInfo(
    val card:CardDetails
)
