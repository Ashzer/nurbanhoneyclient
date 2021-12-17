package org.devjj.platform.nurbanhoney.features.ui.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseEmptyActivity

@AndroidEntryPoint
class ArticleActivity : BaseEmptyActivity() {

    companion object{
        private const val INTENT_EXTRA_PARAM_ARTICLE = "INTENT_PARAM_ARTICLE"
        private const val INTENT_EXTRA_PARAM_BOARD = "INTENT_PARAM_BOARD"
        //fun callingIntent(context: Context) = Intent(context, ArticleActivity::class.java)
        fun callingIntent(context: Context, board : String ,id: Int) =
            Intent(context , ArticleActivity::class.java).apply{
                putExtra(INTENT_EXTRA_PARAM_BOARD, board)
                putExtra(INTENT_EXTRA_PARAM_ARTICLE,id)
            }
    }

    override fun fragment() = ArticleFragment.forArticle(
        intent.getStringExtra(INTENT_EXTRA_PARAM_BOARD),
        intent.getIntExtra(INTENT_EXTRA_PARAM_ARTICLE, -1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)
        setContentView(R.layout.activity_empty)
    }
}