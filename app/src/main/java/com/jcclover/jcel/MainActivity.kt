package com.jcclover.jcel

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController


import com.jcclover.R
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    private var pressedTime: Long = 0
lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //for backbutton in top of the fragment
       navController =  Navigation.findNavController(this,R.id.navhost)
        setupActionBarWithNavController(this@MainActivity,navController)



}

override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp()||super.onSupportNavigateUp()
}

    override fun onBackPressed() {
                if (pressedTime + 1000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        }
        else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }
}