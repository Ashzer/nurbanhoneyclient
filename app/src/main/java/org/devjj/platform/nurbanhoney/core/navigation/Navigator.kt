package org.devjj.platform.nurbanhoney.core.navigation

import android.content.Context
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.core.sharedpreference.Prefs
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.ui.article.ArticleActivity
import org.devjj.platform.nurbanhoney.features.ui.home.HomeActivity
import org.devjj.platform.nurbanhoney.features.ui.home.profile.articles.ProfileArticlesActivity
import org.devjj.platform.nurbanhoney.features.ui.home.profile.comments.ProfileCommentsActivity
import org.devjj.platform.nurbanhoney.features.ui.login.Authenticator
import org.devjj.platform.nurbanhoney.features.ui.login.LoginActivity
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.textedit.TextEditorActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator
@Inject constructor(private val authenticator: Authenticator) {

    private fun showLogin(context: Context) =
        context.startActivity(LoginActivity.callingIntent(context))

    suspend fun showMain(context: Context) {
        when (authenticator.userLoggedIn(Prefs.token)) {
            true -> showHome(context)
            false -> showLogin(context)
        }
    }

    fun showHome(context: Context) =
        context.startActivity(HomeActivity.callingIntent(context))

    fun showProfileArticle(context: Context) =
        context.startActivity(ProfileArticlesActivity.callingIntent(context))

    fun showProfileComment(context: Context) =
        context.startActivity(ProfileCommentsActivity.callingIntent(context))

    suspend fun showTextEditorWithLoginCheck(context: Context, board: Board) {
        fun showTextEditor(context: Context, board: Board) =
            context.startActivity(TextEditorActivity.callingIntent(context,board))
        Log.d("navigation_check__", board.toString())
        when (authenticator.userLoggedIn(Prefs.token)) {
            true -> showTextEditor(context,board)
            false -> showLogin(context)
        }
    }

    suspend fun showTextEditorToModifyWithLoginCheck(context: Context, board: Board, article: Article) {
        fun showTextEditorToModify(context: Context, board: Board, article: Article) {
            val intent = TextEditorActivity.callingIntentToModify(context,board, article)
            val activityOptions = ActivityOptionsCompat.makeBasic()
            context.startActivity(intent, activityOptions.toBundle())
        }

        when (authenticator.userLoggedIn(Prefs.token)) {
            true -> showTextEditorToModify(context, board,article)
            false -> showLogin(context)
        }
    }


    fun showArticle(activity: FragmentActivity, board: Board, id: Int) =
        activity.startActivity(ArticleActivity.callingIntent(activity, board, id))

    fun transFragment(
        supportFragmentManager: FragmentManager,
        frag: BaseFragment,
        containerView: FragmentContainerView
    ) {

        supportFragmentManager.beginTransaction()
            .replace(containerView.id, frag)
            .addToBackStack(frag::class.java.simpleName)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}