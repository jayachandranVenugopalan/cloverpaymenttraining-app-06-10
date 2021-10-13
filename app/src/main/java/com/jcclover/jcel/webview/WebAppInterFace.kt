package com.jcclover.jcel.webview

import android.content.Context
import android.webkit.JavascriptInterface

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