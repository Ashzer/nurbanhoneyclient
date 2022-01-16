package org.devjj.platform.nurbanhoney.features.ui.home.profile.articles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.core.platform.BaseEmptyActivity

@AndroidEntryPoint
class ProfileArticlesActivity : BaseEmptyActivity(){
    override fun fragment() = ProfileArticlesFragment()

    companion object{
        fun callingIntent(context: Context) = Intent(context, ProfileArticlesActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)
    }
}