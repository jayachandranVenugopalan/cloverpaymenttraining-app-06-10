package com.jcclover.jcel.customercarddetails

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.clover.connector.sdk.v3.CardEntryMethods

import com.clover.connector.sdk.v3.PaymentConnector
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.Intents
import com.clover.sdk.v3.connector.ExternalIdUtils
import com.clover.sdk.v3.connector.IPaymentConnectorListener
import com.clover.sdk.v3.payments.TipMode
import com.jcclover.R
import com.clover.sdk.v3.remotepay.*
import com.jcclover.jcel.customerlist.CustomerListDirections
import com.jcclover.jcel.util.AlertDialogue
import com.jcclover.jcel.util.log
import com.jcclover.jcel.util.toast


class SwipeCardPayment : Fragment() {
    private lateinit var paymentConnector: PaymentConnector
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_swipe_card_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        paymentConnector =initializePaymentConnector()
        paymentConnector.initializeConnection()
        var saleRequest: SaleRequest = setupSaleRequest()
        paymentConnector.sale(saleRequest)

    }





    private fun setupSaleRequest(): SaleRequest {
        requireContext().log("enterthe swipefragement","SWIPETAG")
        // Create a new SaleRequest and populate the required request fields
        val saleRequest = SaleRequest()
        saleRequest.externalId = ExternalIdUtils.generateNewID() //required, but can be any string
        saleRequest.amount = 2550
        saleRequest.externalId = ExternalIdUtils.generateNewID()
        saleRequest.cardEntryMethods = Intents.CARD_ENTRY_METHOD_ALL
        saleRequest.disablePrinting = true
        saleRequest.disableReceiptSelection = true
        saleRequest.disableDuplicateChecking = true
        saleRequest.tipAmount = 0L
        saleRequest.allowOfflinePayment = false
        saleRequest.tipMode = TipMode.TIP_PROVIDED
        return saleRequest
    }
    fun initializePaymentConnector(): PaymentConnector {
        // Get the Clover account that will be used with the service; uses the GET_ACCOUNTS permission
        val cloverAccount: Account? = CloverAccount.getAccount(context)
        // Set your RAID as the remoteApplicationId
        val remoteApplicationId ="JMB8M5B10P6N6.7TBNGWY4HZXR2"


        // Implement the other IPaymentConnector listener methods

        // Create the PaymentConnector with the context, account, listener, and RAID
        return PaymentConnector(
            context,
            cloverAccount,
            iPaymentServiceListener,
            remoteApplicationId
        )
    }

    private var iPaymentServiceListener = object : IPaymentConnectorListener {
        override fun onDeviceDisconnected() {
            Toast.makeText(context, "Device Disconnected", Toast.LENGTH_SHORT).show()

        }

        override fun onDeviceConnected() {
            Toast.makeText(context, "Device Connected", Toast.LENGTH_SHORT).show()
//            var alertDialogue=AlertDialogue(requireActivity())
//            alertDialogue.alertmessage()
        }


        override fun onPreAuthResponse(p0: PreAuthResponse?) {
            Toast.makeText(context, "onPreAuthResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthResponse(p0: AuthResponse?) {
            Toast.makeText(context, "onAuthResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onTipAdjustAuthResponse(p0: TipAdjustAuthResponse?) {
            Toast.makeText(context, "onTipAdjustAuthResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onCapturePreAuthResponse(p0: CapturePreAuthResponse?) {
            Toast.makeText(context, "onCapturePreAuthResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onVerifySignatureRequest(p0: VerifySignatureRequest?) {
            Toast.makeText(context, "onVerifySignatureRequest", Toast.LENGTH_SHORT).show()
        }

        override fun onConfirmPaymentRequest(p0: ConfirmPaymentRequest?) {
            Toast.makeText(context, "onConfirmPaymentRequest", Toast.LENGTH_SHORT).show()
        }

        override fun onSaleResponse(response: SaleResponse) {

            val result: String? = if (response.success) {
                "Sale was successful"
            } else {
                "Sale was unsuccessful"
               // + response.reason + ":" + response.message
            }
//            requireActivity().log("${result}","salesResponse")
//            Toast.makeText(
//                context,
//                result!!,
//                Toast.LENGTH_LONG
//            ).show()
onDestroyView()
            onDestroy()
        }

        override fun onManualRefundResponse(p0: ManualRefundResponse?) {
            Toast.makeText(context, "onManualRefundResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onRefundPaymentResponse(p0: RefundPaymentResponse?) {
            Toast.makeText(context, "onRefundPaymentResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onTipAdded(p0: TipAdded?) {
            Toast.makeText(context, "onTipAdded", Toast.LENGTH_SHORT).show()
        }

        override fun onVoidPaymentResponse(p0: VoidPaymentResponse?) {
            Toast.makeText(context, "onVoidPaymentResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onVaultCardResponse(p0: VaultCardResponse?) {
            Toast.makeText(context, "onVaultCardResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onRetrievePendingPaymentsResponse(p0: RetrievePendingPaymentsResponse?) {
            Toast.makeText(context, "onRetrievePendingPaymentsResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onReadCardDataResponse(p0: ReadCardDataResponse?) {
            Toast.makeText(context, "onReadCardDataResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onRetrievePaymentResponse(p0: RetrievePaymentResponse?) {
            Toast.makeText(context, "onRetrievePaymentResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onCloseoutResponse(p0: CloseoutResponse?) {
            Toast.makeText(context, "onCloseoutResponse", Toast.LENGTH_SHORT).show()
        }

        override fun onVoidPaymentRefundResponse(p0: VoidPaymentRefundResponse?) {
            Toast.makeText(context, "onVoidPaymentRefundResponse", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}