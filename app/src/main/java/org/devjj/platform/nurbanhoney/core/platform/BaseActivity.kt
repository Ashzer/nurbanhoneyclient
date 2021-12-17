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

}