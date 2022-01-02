package org.devjj.platform.nurbanhoney.core.navigation

import android.content.Context
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.features.ui.article.Article
import org.devjj.platform.nurbanhoney.features.ui.article.ArticleActivity
import org.devjj.platform.nurbanhoney.features.ui.home.BoardActivity
import org.devjj.platform.nurbanhoney.features.ui.login.Authenticator
import org.devjj.platform.nurbanhoney.features.ui.login.LoginActivity
import org.devjj.platform.nurbanhoney.features.ui.textedit.TextEditorActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator
@Inject constructor(private val authenticator: Authenticator) {
    private fun showLogin(context: Context) =
        context.startActivity(LoginActivity.callingIntent(context))

    suspend fun showMain(context: Context) {
        when (authenticator.userLoggedIn()) {
            true -> showHome(context)
            false -> showLogin(context)
        }
    }

    fun showHome(context: Context) =
        context.startActivity(BoardActivity.callingIntent(context))

    suspend fun showTextEditorWithLoginCheck(context: Context, board: String) {
        fun showTextEditor(context: Context, board: String) =
            context.startActivity(TextEditorActivity.callingIntent(context,board))

        when (authenticator.userLoggedIn()) {
            true -> showTextEditor(context,board)
            false -> showLogin(context)
        }
    }

    suspend fun showTextEditorToModifyWithLoginCheck(context: Context, board: String, article: Article) {
        fun showTextEditorToModify(context: Context, board: String, article: Article) {
            val intent = TextEditorActivity.callingIntentToModify(context,board, article)
            val activityOptions = ActivityOptionsCompat.makeBasic()
            context.startActivity(intent, activityOptions.toBundle())
        }

        when (authenticator.userLoggedIn()) {
            true -> showTextEditorToModify(context, board,article)
            false -> showLogin(context)
        }
    }

    fun transFragment(
        supportFragmentManager: FragmentManager,
        frag: BaseFragment,
        containerView: FragmentContainerView
    ) {

        supportFragmentManager.beginTransaction()
            .replace(containerView.id, frag)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun showArticle(activity: FragmentActivity, board: String, id: Int) =
        activity.startActivity(ArticleActivity.callingIntent(activity, board, id))

}