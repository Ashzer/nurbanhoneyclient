package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_empty.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseEmptyActivity
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.Board

@AndroidEntryPoint
class TextEditorActivity : BaseEmptyActivity() {

    companion object{
        private const val INTENT_EXTRA_PARAM_ARTICLE = "INTENT_PARAM_ARTICLE"
        private const val INTENT_EXTRA_PARAM_BOARD="INTENT_PARAM_BOARD"
        fun callingIntent(context: Context , board : Board) =
            Intent(context, TextEditorActivity::class.java).apply{
                putExtra(INTENT_EXTRA_PARAM_BOARD,board)
            }
        fun callingIntentToModify(context: Context, board : Board, article: Article) =
            Intent(context, TextEditorActivity::class.java).apply {
                putExtra(INTENT_EXTRA_PARAM_BOARD,board)
                putExtra(INTENT_EXTRA_PARAM_ARTICLE , article)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(savedInstanceState)
        setContentView(R.layout.activity_empty)

        setSupportActionBar(emptyToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.texteditor_menu, menu)

       // supportActionBar?.title = (intent.getParcelableExtra(INTENT_EXTRA_PARAM_BOARD) as Board).name
        return super.onCreateOptionsMenu(menu)
    }

    fun setActionBarTitle(title: String){
        supportActionBar?.title = title
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun fragment(): BaseFragment {
        //Log.d("bundle_check__",intent.getStringExtra(INTENT_EXTRA_PARAM_BOARD).toString())
        var board = intent.getParcelableExtra(INTENT_EXTRA_PARAM_BOARD) as Board
        return if(board.address == "nurban")
            try{
                TextEditorNurbanFragment.toModify(board, intent.getParcelableExtra(INTENT_EXTRA_PARAM_ARTICLE))
            }catch(e : Exception){
                TextEditorNurbanFragment.toWrite(board)
            }
        else
            try{
                TextEditorFragment.toModify(board,intent.getParcelableExtra(INTENT_EXTRA_PARAM_ARTICLE))
            }catch(e : Exception){
                TextEditorFragment.toWrite(board)
            }
    }
}