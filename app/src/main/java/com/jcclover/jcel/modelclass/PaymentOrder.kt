package com.jcclover.jcel.modelclass

data class PaymentOrder (var uniqueIdentifiaction:String,var customerName:String,var customerid:String,var amount:String,var paymentStatus:String,var expandable:Boolean,var orderId:String)
data class InvoiceDeatils(var amount:String,var paymentStatus: String,var message:String)