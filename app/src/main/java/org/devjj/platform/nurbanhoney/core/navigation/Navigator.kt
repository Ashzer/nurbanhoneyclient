package org.devjj.platform.nurbanhoney.core.navigation

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.features.ui.article.ArticleActivity
import org.devjj.platform.nurbanhoney.features.ui.home.MainActivity
import org.devjj.platform.nurbanhoney.features.ui.login.Authenticator
import org.devjj.platform.nurbanhoney.features.ui.login.LoginActivity
import org.devjj.platform.nurbanhoney.features.ui.textedit.TextEditorActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator
@Inject constructor(private val authenticator: Authenticator){
    private fun showLogin(context: Context) =
        context.startActivity(LoginActivity.callingIntent(context))

    suspend fun showMain(context: Context){
        when (authenticator.userLoggedIn()){
            true -> showHome(context)
            false -> showLogin(context)
        }
    }

    fun showHome(context: Context) =
        context.startActivity(MainActivity.callingIntent(context))

    suspend fun showTextEditorWithLoginCheck(context: Context){
        when (authenticator.userLoggedIn()){
            true -> showTextEditor(context)
            false -> showLogin(context)
        }
    }
    fun showTextEditor(context: Context) =
        context.startActivity(TextEditorActivity.callingIntent(context))

    fun transFragment(supportFragmentManager : FragmentManager, frag : BaseFragment, containerView: FragmentContainerView){
        supportFragmentManager.beginTransaction()
            .replace(containerView.id, frag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun showArticle(activity: FragmentActivity, id :Int)=
        activity.startActivity(ArticleActivity.callingIntent(activity ,id))

}