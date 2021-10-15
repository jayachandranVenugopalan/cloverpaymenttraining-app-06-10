package com.jcclover.jcel.customerlist.customviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcclover.jcel.LoginViewModel
import com.jcclover.jcel.customercarddetails.CardDetailsViewModel
import com.jcclover.jcel.repository.Repository
import java.lang.IllegalArgumentException

class MainViewModelFactory(var repository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //return MainViewModel(repository) as T
   return when{
        modelClass.isAssignableFrom(MainViewModel::class.java)->MainViewModel(repository as Repository) as T
        modelClass.isAssignableFrom(CardDetailsViewModel::class.java)->CardDetailsViewModel() as T
       modelClass.isAssignableFrom(LoginViewModel::class.java)->LoginViewModel() as T
       modelClass.isAssignableFrom(CustomerListViewModel::class.java)->CustomerListViewModel() as T

       else->throw IllegalArgumentException("viewmodel is notfound")
    }

    }
}