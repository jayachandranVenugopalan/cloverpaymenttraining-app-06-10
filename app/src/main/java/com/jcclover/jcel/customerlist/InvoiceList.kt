package com.jcclover.jcel.customerlist

import com.jcclover.jcel.modelclass.PaymentOrder

class InvoiceList {
     fun createList() : ArrayList<PaymentOrder> {

       var  paymentdetailsList = ArrayList<PaymentOrder>()
        paymentdetailsList.add(PaymentOrder("","Jai","100","4500","",false,"","Jai5603"))
        paymentdetailsList.add(PaymentOrder("","Chandran","101","6250","",false,"","Chandran5604"))
        paymentdetailsList.add(PaymentOrder("","Ravi","102","1235","",false,"","Ravi5605"))
        paymentdetailsList.add(PaymentOrder("","Carclo","103","4500","",false,"","Carclo5606"))
        paymentdetailsList.add(PaymentOrder("","MUTLILINk","104","2250","",false,"","MUTLILINk5607"))
        paymentdetailsList.add(PaymentOrder("","Carspa","105","1250","",false,"","Carspa5608"))
        paymentdetailsList.add(PaymentOrder("","Metro","106","6000","",false,"","Metro5609"))
        paymentdetailsList.add(PaymentOrder("","Wriston","107","1500","",false,"","Wriston5610"))
        paymentdetailsList.add(PaymentOrder("","Elastomers","108","4500","",false,"","Elastomers5611"))
        paymentdetailsList.add(PaymentOrder("","Mrp","109","2450","",false,"","Mrp5612"))
        paymentdetailsList.add(PaymentOrder("","Rub","110","8450","",false,"","Rub5613"))
        return paymentdetailsList
    }
}