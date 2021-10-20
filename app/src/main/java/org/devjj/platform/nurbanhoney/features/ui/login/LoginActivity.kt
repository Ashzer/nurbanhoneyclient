package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.core.platform.BaseActivity
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inTransaction

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    companion object{
        fun callingIntent(context : Context) = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction {
            add(
                R.id.LoginFragmentContainer,fragment()
            )
        }

    fun fragment() = LoginFragment()
}