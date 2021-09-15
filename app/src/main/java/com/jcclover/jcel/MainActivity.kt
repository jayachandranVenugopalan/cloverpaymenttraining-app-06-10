package com.jcclover.jcel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jcclover.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


   // setupActionBarWithNavController(findNavController(R.id.navhost))
}
//
//override fun onSupportNavigateUp(): Boolean {
//    val navController=findNavController(R.id.navhost)
//    return navController.navigateUp()||super.onSupportNavigateUp()
//}
}