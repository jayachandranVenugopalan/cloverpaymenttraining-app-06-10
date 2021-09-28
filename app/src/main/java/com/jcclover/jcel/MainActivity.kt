package com.jcclover.jcel

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController


import com.jcclover.R
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    private var pressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


}

override fun onSupportNavigateUp(): Boolean {
    val navController=findNavController(R.id.navhost)
    return navController.navigateUp()||super.onSupportNavigateUp()
}

    override fun onBackPressed() {
        super.onBackPressed()
    }

    //        if (pressedTime + 2000 > System.currentTimeMillis()) {
//            super.onBackPressed()
//            finish()
//        }
//
//        else {
//            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
//        }
//        pressedTime = System.currentTimeMillis()

}