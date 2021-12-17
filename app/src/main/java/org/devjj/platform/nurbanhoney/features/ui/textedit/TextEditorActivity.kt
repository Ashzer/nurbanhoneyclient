package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseEmptyActivity
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.features.ui.article.Article

@AndroidEntryPoint
class TextEditorActivity : BaseEmptyActivity() {

    companion object{
        private const val INTENT_EXTRA_PARAM_ARTICLE = "INTENT_PARAM_ARTICLE"
        private const val INTENT_EXTRA_PARAM_BOARD="INTENT_PARAM_BOARD"
        fun callingIntent(context: Context , board : String) =
            Intent(context, TextEditorActivity::class.java).apply{
                putExtra(INTENT_EXTRA_PARAM_BOARD,board)
            }
        fun callingIntentToModify(context: Context, board : String , article: Article) =
            Intent(context, TextEditorActivity::class.java).apply {
                putExtra(INTENT_EXTRA_PARAM_BOARD,board)
                putExtra(INTENT_EXTRA_PARAM_ARTICLE , article)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)
        setContentView(R.layout.activity_empty)
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun fragment(): BaseFragment {
        return try{
            TextEditorFragment.toModify(intent.getParcelableExtra(INTENT_EXTRA_PARAM_ARTICLE))
        }catch(e : Exception){
            TextEditorFragment()
        }
    }
}