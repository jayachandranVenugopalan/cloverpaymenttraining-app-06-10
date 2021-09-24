package com.jcclover.jcel.modelclass

data class PaymentOrder (var uniqueIdentifiaction:String,var customerName:String,var customerid:String,var amount:String,var paymentStatus:String,var expandable:Boolean,var orderId:String,var invoiceNO:String)
data class InvoiceDeatils(var amount:String,var paymentStatus: String,var message:String)



//RestApi 1
data class Post(


    var userId:Int,
    var id:Int,
    var title:String,
    var body:String

)

data class Merchent(

    var href:String,
    var id:String,
    var name:String
)

data class GetOrders(


   // var object :String
var url:String

)


data class cards(

    var first6:String,
    var last4:String,
    var firstName:String,
    var lastName:String,
    var expirationDate:String,
    var cardType:String
)

data class CreateCustomerResponse(
    var href:String,
    var id:String,
    var marketingAllowed:Boolean,
    var customerSince:String,
    var cards: cards

)

data class OwnId(

    var mid:String
)

data class PaymentResponse(
   var message:String
)