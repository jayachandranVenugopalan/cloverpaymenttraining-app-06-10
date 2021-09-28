package com.jcclover.jcel.modelclass

data class PaymentOrder (var uniqueIdentifiaction:String,var customerName:String,var customerid:String,var amount:String,var paymentStatus:String,var expandable:Boolean,var orderId:String,var invoiceNO:String)
data class InvoiceDeatils(var amount:String,var paymentStatus: String,var message:String)



//RestApi 1






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



data class Charges(
    var ecomind:String,
    var amount: Int,
    var currency:String,
    var source:String

)
data class TokenResponse(
    var id: String,
    var `object` : String,
    var card:Tokencard,
    var message: String
)
data class Tokencard(
    var exp_month:String,
var exp_year:String,
 var first6: String,
var last4: String,
var brand: String
)

data class OrderDetails(
    var id:String,
    var amount:String,
    var amount_refunded:String,
    var currency:String, var created:String, var captured:Boolean,
    var ref_num:String,
    var auth_code:String,

)


data class ErrorResponse(
    var message: String,
    var error:Errortitle
)

data class Errortitle(
    var type:String,
    var code:String,
    var message:String
)