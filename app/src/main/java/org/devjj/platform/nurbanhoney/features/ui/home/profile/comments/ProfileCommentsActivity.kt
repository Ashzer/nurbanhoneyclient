package org.devjj.platform.nurbanhoney.features.ui.home.profile.comments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.core.platform.BaseEmptyActivity

@AndroidEntryPoint
class ProfileCommentsActivity : BaseEmptyActivity(){
    override fun fragment() = ProfileCommentsFragment()

    companion object{
        fun callingIntent(context: Context) = Intent(context, ProfileCommentsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)

        setToolbarTitle("내 댓글")
    }
}