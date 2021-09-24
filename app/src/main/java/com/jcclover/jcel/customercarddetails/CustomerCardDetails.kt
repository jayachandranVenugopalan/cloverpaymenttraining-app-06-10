package com.jcclover.jcel.customercarddetails

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.jcclover.R
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.modelclass.cards
import com.jcclover.jcel.repository.Repository
import com.jcclover.jcel.util.Constant.Companion.CARDTYPE
import com.jcclover.jcel.util.Constant.Companion.CUSTOMERID
import com.jcclover.jcel.util.Constant.Companion.LAST4


class CustomerCardDetails : Fragment() {
    private lateinit var  viewModel:MainViewModel
    var sucess=false
    var editor:SharedPreferences.Editor?=null
    var sharedpreferences: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        sharedpreferences=requireActivity().getSharedPreferences("mylab",MODE_PRIVATE)
      editor =sharedpreferences!!.edit()
        var customerID:String?=  sharedpreferences!!.getString(CUSTOMERID,"")
        Log.d("msg customerID","${customerID}")
        val repository= Repository()
        val viewModelFactory= MainViewModelFactory(repository)
        viewModel= ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
        var first6:EditText=view.findViewById(R.id.et_first6)
        var last4:EditText=view.findViewById(R.id.et_last4)
        var firstName:EditText=view.findViewById(R.id.firstname_et)
        var lastName:EditText=view.findViewById(R.id.lastname_et)
        var experiationDate:EditText=view.findViewById(R.id.et_experiationdate)
        var cardtype:EditText=view.findViewById(R.id.et_cardtype)
        view.findViewById<Button>(R.id.save_btn).setOnClickListener {

            editor!!.putString(LAST4,last4.text.toString())
            editor!!.putString(CARDTYPE,cardtype.text.toString())
            editor!!.apply()
            editor!!.commit()
            createCustmerDetails( first6.text.toString(),last4.text.toString(),firstName.text.toString(),lastName.text.toString(),experiationDate.text.toString(),cardtype.text.toString())
          //  getMerchentOrders()
        }


    }
    private fun getMerchentOrders() {

//viewModel.getPost()
// viewModel.getPostdemo()
//viewModel.setPost(Post(2,2,"jai using post","postmethod"))
        var id="83H8Q583NRCK1"
        viewModel.getPost1(id.toString())

        viewModel.myReponse.observe(requireActivity(), Observer { response->
            Log.d("Responsed","enters")
            if(response.isSuccessful) {
                Log.d("Responsed is sucess", response.body().toString())
                Log.d("Responsed is sucess", response.headers().toString())
            }else{
                Log.d("Responsed is failure", response.errorBody().toString())
                Log.d("Responsed is failure", response.message().toString())
                Log.d("Responsed is failure", response.code().toString())
            }

        })
    }

    private fun createCustmerDetails(
        first6: String,
        last4: String,
        firstname: String,
        lastname: String,
        expirationdate: String,
    cardtype:String
    ) {
        var cards1=cards(first6,last4,firstname,lastname,expirationdate,cardtype)

        Log.d("Responsed is sucess", cards1.toString())
        if ( first6.isNullOrEmpty()&&last4.isNullOrEmpty()&&firstname.isNullOrEmpty()&&lastname.isNullOrEmpty()&&expirationdate.isNullOrEmpty()){
            Toast.makeText(activity, "Enter the all the values", Toast.LENGTH_SHORT).show()
        }
        else{
        viewModel.createCustomerDetails(cards1)
        viewModel.myCreatrCustomerResponse.observe(requireActivity(), Observer {

                response->

            if (response.isSuccessful){
                Log.d("Responsed is sucess", response.body().toString())
                Log.d("Responsed is sucess", response.body()?.id.toString())
                Log.d("Responsed is sucess", response.headers().toString())
var cusid=response.body()?.id.toString()
                editor!!.putString(CUSTOMERID,cusid)
                editor!!.apply()
                editor!!.commit()
               sucess=true

                Navigation.findNavController(requireActivity(),R.id.navhost)
                    .navigate(R.id.action_customerCardDetails_to_customerList)
            }else{
                Log.d("Responsed is failure", response.errorBody().toString())
                Log.d("Responsed is failure", response.message().toString())
                Log.d("Responsed is failure", response.code().toString())
                sucess=false
            }
        })

    }

    }

}