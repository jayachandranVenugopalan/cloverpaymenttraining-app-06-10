package com.jcclover.jcel.webview

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import androidx.navigation.Navigation.findNavController

import androidx.navigation.fragment.findNavController
import com.jcclover.jcel.customercarddetails.CustomerCardDetails
import com.jcclover.jcel.customercarddetails.CustomerCardDetailsDirections
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent

class WebAppInterFace(ctx:Context) {

   var mcontex:Context
  var data:String?=null
    init {
        this.mcontex=ctx
    }
    @JavascriptInterface
    fun sendData(data:String){
        this.data=data
        RxBus1.publish(RxEvent.PaymentToken(data))

    }

}