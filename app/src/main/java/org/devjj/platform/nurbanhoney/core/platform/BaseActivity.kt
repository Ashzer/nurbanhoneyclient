package org.devjj.platform.nurbanhoney.core.platform

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inTransaction

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    abstract fun addFragment(savedInstanceState: Bundle?) : Any
    abstract fun fragment() : BaseFragment

    private var backPressedTime = 0L
    override fun onBackPressed() {
        if(System.currentTimeMillis() > backPressedTime + 2000){
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "The app will be terminated when pressed Back again",Toast.LENGTH_SHORT).show()
        }else if(System.currentTimeMillis() <= backPressedTime + 2000){
            finish()
        }
    }
}