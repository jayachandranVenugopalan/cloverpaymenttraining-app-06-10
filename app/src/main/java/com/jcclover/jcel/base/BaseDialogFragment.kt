package com.jcclover.jcel.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.jcclover.R
import com.jcclover.databinding.CustomdialogBinding
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.screens.dialog.InfoDialogFragment
import com.jcclover.jcel.ui.CustomerListDirections
import com.jcclover.jcel.util.AlertDialogue
import com.jcclover.jcel.util.log
import com.jcclover.jcel.util.toast
import java.util.zip.Inflater

open class BaseDialogFragment:DialogFragment() {

}

