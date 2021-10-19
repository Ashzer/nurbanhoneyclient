package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.devjj.platform.nurbanhoney.core.platform.BaseActivity
import org.devjj.platform.nurbanhoney.R

class LoginActivity : BaseActivity() {
    companion object{
        fun callingIntent(context : Context) = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun fragment() = LoginFragment()
}