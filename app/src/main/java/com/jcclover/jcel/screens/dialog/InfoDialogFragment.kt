package com.jcclover.jcel.screens.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import com.jcclover.R
import com.jcclover.jcel.base.BaseDialogFragment
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.util.AlertDialogue
import com.jcclover.jcel.util.toast

class InfoDialogFragment : BaseDialogFragment() {
 var amount:String="3780"
 var position: Int=0
    companion object {

        private const val DIALOG_TITLE = "DIALOG_TITLE"
        private const val DIALOG_MESSAGE = "DIALOG_MESSAGE"
        private const val DIALOG_BUTTON = "DIALOG_BUTTON"
        private const val DIALOG_POSITIVE_EVENT = "DIALOG_POSITIVE_EVENT"
        fun getInfoDialog(title:String, message : String, buttonText: String,positiveEvent: RxEvent.DialogEventEnum):
                InfoDialogFragment {
            var infoDialogFragment = InfoDialogFragment()
            val args = Bundle().also {
                it.putString(
                    DIALOG_TITLE,
                    title
                )
                it.putString(
                    DIALOG_MESSAGE,
                    message
                )
                it.putString(
                    DIALOG_BUTTON,
                    buttonText
                )
                it.putSerializable(
                    DIALOG_POSITIVE_EVENT,
                    positiveEvent
                )
            }
            infoDialogFragment.arguments = args
            return infoDialogFragment
        }
    }
   // var title=arguments?.getString("DIALOG_TITLE","payment")
fun dataneed(amount: String, position: Int) {
    this.amount=amount
      this.position= position
}

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments == null) {
            throw Exception(getString(R.string.can_not_show_dialog_without_argument))
        }
        var dialog = Dialog(requireContext())
           dialog.setContentView(R.layout.customdialog)
            dialog.window?.setLayout(750,500)

        dialog.findViewById<Button>(R.id.btn_okay).setOnClickListener{
            requireActivity().toast("Manual card Entry")

            RxBus1.publish(RxEvent.EventDialog(requireArguments().getSerializable(DIALOG_POSITIVE_EVENT)
                    as RxEvent.DialogEventEnum, null))
          // RxBus1.publish(RxEvent.success("manual is clicked",amount, position))
            dialog.dismiss()
        }
            dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener{
                requireActivity().toast("Swipe Entry")
            var alertDialogue = AlertDialogue(requireActivity())
            alertDialogue.alertmessage()
            dialog?.dismiss()
            }


        return dialog
    }



//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding= CustomdialogBinding.inflate(layoutInflater,container,false)
//
//        requireContext().toast("$title")
//
//        binding.title?.text=title.toString()
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.title?.text=arguments?.getString("DIALOG_TITLE","payment")
//
//        dialog?.window?.setLayout(750,500)
//        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//
//
//        binding.btnCancel.setOnClickListener {
//            requireActivity().toast("Swipe Entry")
//            var alertDialogue = AlertDialogue(requireActivity())
//            alertDialogue.alertmessage()
//            dialog?.dismiss()  }
//
//        binding.btnOkay.setOnClickListener {
//            requireActivity().toast("Manual card Entry")
//            RxBus1.publish(RxEvent.success("manual is clicked"))
//            dialog?.dismiss()
//        }
//
//
//    }



}