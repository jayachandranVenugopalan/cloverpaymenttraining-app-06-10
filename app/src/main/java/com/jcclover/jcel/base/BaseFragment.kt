package com.jcclover.jcel.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.ViewDataBindingKtx
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewbinding.ViewBinding
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModel
import com.jcclover.jcel.customerlist.customviewmodel.MainViewModelFactory
import com.jcclover.jcel.repository.Repository
import java.text.FieldPosition

abstract class BaseFragment <VM:BaseViewModel,B: ViewBinding>:Fragment(){

    protected  lateinit var binding:B
    protected lateinit  var viewModel:VM

  abstract fun getViewModel():Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater,container: ViewGroup?):B







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        binding=getFragmentBinding(inflater,container)
        val factory=MainViewModelFactory(Repository())
        viewModel=ViewModelProvider(this,factory).get(getViewModel())


        return binding.root
            }

fun showToast(message:String){
    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
}
}