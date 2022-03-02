package org.devjj.platform.nurbanhoney.core.platform

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    abstract fun addFragment(savedInstanceState: Bundle?): Any
    abstract fun fragment(): BaseFragment

    private var backPressedTime = 0L

    override fun onBackPressed() {
        when (supportFragmentManager.backStackEntryCount) {
            1 -> {
                this.finish()
            }
            0 -> {
                super.onBackPressed()
                Log.d(
                    "onBackPressed_check",
                    "performed super.onBackPressed. supportFragmentManager.backStackEntryCount = ${supportFragmentManager.backStackEntryCount}"
                )
            }
            else -> {
                var count = supportFragmentManager.backStackEntryCount
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.backStackEntryCount == count) supportFragmentManager.popBackStack()

                Log.d(
                    "onBackPressed_check",
                    "performed supportFragmentManager.popBackStack(). supportFragmentManager.backStackEntryCount = ${supportFragmentManager.backStackEntryCount}"
                )
            }
        }
    }
}