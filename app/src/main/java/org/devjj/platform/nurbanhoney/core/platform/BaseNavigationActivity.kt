package org.devjj.platform.nurbanhoney.core.platform

import android.app.Fragment
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inTransaction

@AndroidEntryPoint
abstract class BaseNavigationActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

    }
    override fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction {
            replace(
                R.id.navigationFragmentContainer,fragment()
            )
        }


}