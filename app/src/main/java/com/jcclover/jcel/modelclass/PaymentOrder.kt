package com.jcclover.jcel.modelclass

class PaymentOrder (uniqueIdentifiaction:String,customerName:String,customerid:String,amount:String,paymentStatus:String,expandable:Boolean){

        var uniqueIdentifiaction:String=""
    var customerName:String=""
    var customerid:String=""
    var amount:String=""
    var paymentStatus:String=""
var expandable:Boolean=false
    init {
        this.uniqueIdentifiaction=uniqueIdentifiaction
        this.customerName=customerName
        this.customerid=customerid
        this.amount=amount
        this.paymentStatus=paymentStatus
this.expandable=expandable

    }



}