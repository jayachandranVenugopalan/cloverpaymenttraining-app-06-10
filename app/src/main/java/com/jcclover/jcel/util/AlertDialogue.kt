package com.jcclover.jcel.util

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.navigation.Navigation.findNavController

import androidx.navigation.fragment.findNavController
import com.jcclover.R
import com.jcclover.jcel.customerlist.CustomerList
import com.jcclover.jcel.customerlist.CustomerListDirections

class AlertDialogue(ctx: Context) {
    val builder = AlertDialog.Builder(ctx)
 var activity:Context
 var alertStatus=false
init {
    this.activity=ctx
}
    fun alertmessage() {


        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialogMessageSwipe)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Ok") { dialogInterface, which ->


        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

}
