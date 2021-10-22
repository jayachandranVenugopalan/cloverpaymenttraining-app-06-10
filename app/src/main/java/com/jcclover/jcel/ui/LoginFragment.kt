package com.jcclover.jcel.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jcclover.R
import com.jcclover.databinding.FragmentLoginBinding
import com.jcclover.jcel.LoginViewModel
import com.jcclover.jcel.base.BaseDialogFragment
import com.jcclover.jcel.base.BaseFragment


class LoginFragment : BaseFragment<LoginViewModel,FragmentLoginBinding,BaseDialogFragment> (){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         viewModel.logincall(binding,requireContext())
        binding.SubmitBtn.setOnClickListener{
           
           var action=LoginFragmentDirections.actionLoginFragmentToCustomerList()
           findNavController().navigate(action)
        }
    }

    override fun getViewModel()=LoginViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    )= FragmentLoginBinding.inflate(inflater,container,false)

    override fun getDialog()=BaseDialogFragment()
}