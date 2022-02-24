package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseEmptyActivity

class LoginActivity : BaseEmptyActivity() {

    companion object{
        fun callingIntent(context : Context) = Intent(context, LoginActivity::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)
    }



    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
    override fun fragment() = LoginFragment()


}