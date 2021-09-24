package com.jcclover.jcel.customerlist

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcclover.R
import com.jcclover.jcel.Implementation.OnServiceClickListner
import com.jcclover.jcel.event.RxBus1
import com.jcclover.jcel.event.RxEvent
import com.jcclover.jcel.modelclass.PaymentOrder
import org.w3c.dom.Text
import java.util.ArrayList

class CustomerListAdaptor(var paymentDetailsList:ArrayList<PaymentOrder>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isExpandable:Boolean=false
    private var onClickListner: OnServiceClickListner?=null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var customerList =LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
           return InvoiceViewHolder(customerList)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is InvoiceViewHolder) {
            holder.setName((paymentDetailsList[position] ),onClickListner,position)
            holder.amount.setText("${(paymentDetailsList[position] as PaymentOrder).amount}$")
            holder.customerId.setText("${(paymentDetailsList[position] as PaymentOrder).customerid}")
            holder.paymentStaus.setText("${(paymentDetailsList[position] as PaymentOrder).paymentStatus}")
            holder.orderId.setText((paymentDetailsList[position]as PaymentOrder).uniqueIdentifiaction)
            holder.invoiceno.setText((paymentDetailsList[position] as PaymentOrder).invoiceNO)
        holder.linearLayout.setOnClickListener {
            Log.d("msg","clicked position${position}")
            var paymentOrder:PaymentOrder= paymentDetailsList.get(position) as PaymentOrder
        paymentOrder.expandable=!paymentOrder.expandable

        notifyItemChanged(position)

            if (holder.paymentStaus.text=="Paid"){
                holder.btn_Details.visibility=View.GONE
                with(holder) {
                    paymentStaus.setTextColor(Color.parseColor("#4CAF50"))
                }
            }else{
                holder.btn_Details.visibility=View.VISIBLE
                holder.paymentStaus.setTextColor(Color.parseColor("#E1AF1B"))
            }
            notifyItemChanged(position)

        }

            isExpandable=((paymentDetailsList.get(position) as PaymentOrder).expandable)
            Log.d("msg","clicked position ${isExpandable}")
            holder.expandableLayout.setVisibility(
                when(isExpandable){true->View.VISIBLE
                    false->View.GONE }
            )
            if (holder.paymentStaus.text=="Paid"){
                holder.btn_Details.visibility=View.GONE
                with(holder) {
                    paymentStaus.setTextColor(Color.parseColor("#4CAF50"))
                }
            }else{
                holder.btn_Details.visibility=View.VISIBLE
                holder.paymentStaus.setTextColor(Color.parseColor("#E1AF1B"))
            }
        }

    }
    override fun getItemCount(): Int {
        return paymentDetailsList.size
    }

    class InvoiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var customerName:TextView=view.findViewById(R.id.customer_name)
       var customerId:TextView=view.findViewById(R.id.cus_id)
        var amount:TextView=view.findViewById(R.id.cus_amount)
        var paymentStaus:TextView=view.findViewById(R.id.cus_payment_status)
        var orderId:TextView=view.findViewById(R.id.order_id)
        var btn_Details:TextView=view.findViewById(R.id.btn_details)
        var expandableLayout:RelativeLayout=view.findViewById(R.id.expandablelayout)
        var linearLayout:LinearLayout=view.findViewById(R.id.linear_layout)
        var invoiceno:TextView=view.findViewById(R.id.invoice_no)
        fun setName( service:Any,listener: OnServiceClickListner?,position: Int) {
            customerName.text=(service as PaymentOrder).customerName
            btn_Details.setOnClickListener {
            listener?.onServiceClicked(service,position)
        }
        }

    }

    public  fun setonClickListner(listener:OnServiceClickListner){
        onClickListner = listener
    }


}
