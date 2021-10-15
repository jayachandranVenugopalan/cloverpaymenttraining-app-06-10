package com.jcclover.jcel.util

import android.accounts.Account
import android.app.Application
import androidx.fragment.app.Fragment
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.order.OrderConnector

class MyCustomApplication:Application() {
    private var mAccount: Account? = null
    var mOrderConnector: OrderConnector? = null
    override fun onCreate() {
        super.onCreate()
        getCloverAccount()
        log("in application class","application class")
        connect()
    }
    private fun getCloverAccount() {
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this)
            if (mAccount == null) {
                return
            }
        }
    }



    private fun connect() {
        disconnect()
        if (mAccount != null) {
            mOrderConnector = OrderConnector(this, mAccount, null)
            mOrderConnector!!.connect()
        }
    }

    private fun disconnect() {
        if (mOrderConnector != null) {
            mOrderConnector!!.disconnect()
            mOrderConnector = null
        }
    }

}