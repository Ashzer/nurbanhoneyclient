package org.devjj.platform.nurbanhoney.core.platform

import android.os.Bundle
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inTransaction

abstract class BaseEmptyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
    }

    override fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction {
            add(
                R.id.EmptyFragmentContainer, fragment()
            )
        }
}