package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseEmptyActivity
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment

@AndroidEntryPoint
class TextEditorActivity : BaseEmptyActivity() {

    companion object{
        fun callingIntent(context: Context) =
            Intent(context, TextEditorActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)
        setContentView(R.layout.activity_empty)
    }

    override fun fragment(): BaseFragment = TextEditorFragment()
}