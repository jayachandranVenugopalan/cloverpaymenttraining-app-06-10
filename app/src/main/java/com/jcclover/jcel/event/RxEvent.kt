package com.jcclover.jcel.event

import com.jcclover.jcel.modelclass.PaymentOrder

class RxEvent {
    enum class DialogEventEnum {
        PAYMENT_PAGE, GO_BACK, NEUTRAL, DISMISS, POSITIVE, NEGATIVE, PLACE_ORDER, CANCEL
    }
    data class EventDialog(val dialogEvent: DialogEventEnum, val any: Any?)

    data class CustomerDetail(var selectedCustomerDetail: PaymentOrder, var position: Int)
    data class selecteddetails(var seletected: PaymentOrder,var position: Int)
    data class OrderId(var orderId:String,var amount:String,var position: Int)
    data class ResponseOrderId(var orderId:String,var amount:String,var position: Int)
    data class PaymentToken(var token:String)
    data class chargesSucess(var orderID:String,var message:String)
    data class success(var successmesg:String,var amount:String,var position: Int)
}