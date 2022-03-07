package org.devjj.platform.nurbanhoney.features.ui.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.forEach
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseEmptyActivity
import org.devjj.platform.nurbanhoney.features.Board

@AndroidEntryPoint
class ArticleActivity : BaseEmptyActivity() {
    private lateinit var actionMenu: Menu
    var modifyArticleClickListener: () -> Unit = {}
    var deleteArticleClickListener: () -> Unit = {}

    companion object {
        private const val INTENT_EXTRA_PARAM_ARTICLE = "INTENT_PARAM_ARTICLE"
        private const val INTENT_EXTRA_PARAM_BOARD = "INTENT_PARAM_BOARD"

        //fun callingIntent(context: Context) = Intent(context, ArticleActivity::class.java)
        fun callingIntent(context: Context, board: Board, id: Int) =
            Intent(context, ArticleActivity::class.java).apply {
                putExtra(INTENT_EXTRA_PARAM_BOARD, board)
                putExtra(INTENT_EXTRA_PARAM_ARTICLE, id)
            }
    }

    override fun fragment() = ArticleFragment.forArticle(
        intent.getParcelableExtra(INTENT_EXTRA_PARAM_BOARD),
        intent.getIntExtra(INTENT_EXTRA_PARAM_ARTICLE, -1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)

        var board = (intent.getParcelableExtra(INTENT_EXTRA_PARAM_BOARD) as Board?) ?: Board.empty
        Log.d("bundle_check__", board.toString())

        setToolbarTitle(board.name)
        
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_toolbar_menu, menu)
        if (menu != null)
            actionMenu = menu
        return true
    }

    fun hideActionMenu() {
        actionMenu.forEach {
            it.isVisible = false
        }
    }

    fun showActionMenu() {
        actionMenu.forEach {
            it.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_article_edit->{
                modifyArticleClickListener()
            }
            R.id.action_article_delete->{
                deleteArticleClickListener()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}