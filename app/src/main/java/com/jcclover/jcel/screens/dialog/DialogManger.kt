package com.jcclover.jcel.screens.dialog

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.jcclover.R
import com.jcclover.jcel.event.RxEvent

class DialogManger (var context: Context, var fragmentManager: FragmentManager) {
    val APP_EXIT_DIALOG = "APP_EXIT_DIALOG"
    fun showAppExitDialog(amount:String,position:Int,tag: String?) {
var infoDialogFragment=InfoDialogFragment()
        Log.d("entered position $position","entered position")
        infoDialogFragment.dataneed(amount,position)
        InfoDialogFragment.getInfoDialog(
            context.getString(R.string.title_close_app),
            context.getString(R.string.message_close_app),
            context.getString(R.string.info_dialog_button_ok),
            RxEvent.DialogEventEnum.PAYMENT_PAGE

        ).show(fragmentManager, tag)
    }


}