package com.jcclover.jcel.event

import com.jcclover.jcel.modelclass.PaymentOrder

class RxEvent {
    enum class DialogEventEnum {
        EXIT_APP, GO_BACK, NEUTRAL, DISMISS, POSITIVE, NEGATIVE, PLACE_ORDER, CANCEL
    }

    data class CustomerDetail(var selectedCustomerDetail: PaymentOrder, var position: Int)
    data class selecteddetails(var seletected: PaymentOrder,var position: Int)
}