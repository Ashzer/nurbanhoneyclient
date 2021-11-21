package org.devjj.platform.nurbanhoney.core.platform

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inTransaction

@AndroidEntryPoint
abstract class BaseTabLayoutActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)
    }
    override fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction {
            add(
                R.id.TabLayoutFragmentContainer,fragment()
            )
        }
}