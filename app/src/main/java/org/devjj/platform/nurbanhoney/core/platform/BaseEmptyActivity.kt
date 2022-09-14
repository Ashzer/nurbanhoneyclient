package org.devjj.platform.nurbanhoney.core.platform

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inTransaction
import org.devjj.platform.nurbanhoney.databinding.ActivityEmptyBinding

@AndroidEntryPoint
abstract class BaseEmptyActivity : BaseActivity() {

    private lateinit var binding: ActivityEmptyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmptyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.emptyToolbar)
    }

    protected fun setToolbarTitle(text: String) {
        supportActionBar?.title = text
    }

    override fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction {
            replace(
                R.id.emptyFragmentContainer, fragment()
            )
        }
}