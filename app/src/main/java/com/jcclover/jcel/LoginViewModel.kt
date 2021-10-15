package com.jcclover.jcel

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController

import com.jcclover.R
import com.jcclover.databinding.FragmentLoginBinding
import com.jcclover.jcel.ui.LoginFragmentDirections

class LoginViewModel:ViewModel() {

   fun logincall(binding: FragmentLoginBinding, requireContext: Context) {

      var username= binding.username.text.toString()
       Log.d("this","requireContext $username")
       }

   }
