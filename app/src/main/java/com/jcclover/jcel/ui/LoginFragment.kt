package com.jcclover.jcel.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.jcclover.R
import com.jcclover.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
private lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getActivity()?.setTitle("JcelCloverpay");
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.SubmitBtn.setOnClickListener{
        findNavController().navigate(R.id.action_loginFragment_to_customerList)
        }
    }
}