package com.jcclover.jcel.modelclass

class SelectedCustomerDetail  (uniqueIdentifiaction:String,customerName:String,customerid:String,amount:String,paymentStatus:String){

    var uniqueIdentifiaction:String=""
    var customerName:String=""
    var customerid:String=""
    var amount:String=""
    var paymentStatus:String=""

    init {
        this.uniqueIdentifiaction=uniqueIdentifiaction
        this.customerName=customerName
        this.customerid=customerid
        this.amount=amount
        this.paymentStatus=paymentStatus


    }
}