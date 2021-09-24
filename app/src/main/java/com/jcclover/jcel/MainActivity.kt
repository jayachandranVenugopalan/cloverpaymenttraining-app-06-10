package com.jcclover.jcel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController


import com.jcclover.R

class MainActivity : AppCompatActivity() {
    private var pressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // setupActionBarWithNavController(findNavController(R.id.navhost))

}

override fun onSupportNavigateUp(): Boolean {
    val navController=findNavController(R.id.navhost)
    return navController.navigateUp()||super.onSupportNavigateUp()
}

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }
}