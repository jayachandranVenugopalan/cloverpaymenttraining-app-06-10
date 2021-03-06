package com.jcclover.jcel.customercarddetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.jcclover.databinding.FragmentCustomerCardDetailsBinding
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.util.Constant
import com.jcclover.jcel.webview.MyWebViewClient
import com.jcclover.jcel.webview.WebAppInterFace
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CardDetailsViewModel: ViewModel() {
    var scope= CoroutineScope(CoroutineName("MyScope") + Dispatchers.Main)
private lateinit var binding: FragmentCustomerCardDetailsBinding
private lateinit var requireContext: Context
    private lateinit var  viewModel:MainViewModel
private lateinit var cardDetailsInterface: CardDetailsInterface
fun callwebView(binding: FragmentCustomerCardDetailsBinding, requireContext: Context) {
   this.binding=binding
    this.requireContext=requireContext
    binding.webView.apply {
        settings.javaScriptEnabled=true
        addJavascriptInterface(WebAppInterFace(requireContext),"Android")
        webViewClient = MyWebViewClient(requireContext)
        loadUrl(Constant.BASE_URL_WEBVIEW)
    }
}


    val paymentchargeswithToken: BroadcastReceiver =object : BroadcastReceiver(){

        override fun onReceive(ctx: Context?, intent: Intent?) {
            scope.launch{
                binding.progressbar.visibility= View.VISIBLE
                binding.webView.visibility= View.GONE
                var paymentToken:String?=intent!!.getStringExtra("token")
//                createChanges(paymentToken!!,args.amount,args.position)
                cardDetailsInterface.sendingdata(paymentToken)
            }
        }
    }




}