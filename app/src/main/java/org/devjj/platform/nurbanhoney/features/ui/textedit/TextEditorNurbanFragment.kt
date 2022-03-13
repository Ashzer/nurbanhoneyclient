package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.visible
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.Board

class TextEditorNurbanFragment : TextEditorFragment() {

    companion object {
        private const val PARAM_ARTICLE = "param_article"
        private const val PARAM_BOARD = "param_board"

        fun toModify(board: Board, article: Article) = TextEditorNurbanFragment().apply {
            arguments =
                bundleOf(PARAM_ARTICLE to article, PARAM_BOARD to board)
        }

        fun toWrite(board: Board) = TextEditorNurbanFragment().apply {
            arguments = bundleOf(PARAM_BOARD to board)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textEditorNurbanLossCutClo.visible()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.writing_done -> {

                val thumbnailUrl = viewModel.searchThumbnail(mEditor.html.toString())
//                Log.d("match_check__", thumbnailUrl)
                if (isModify) {
                    viewModel.modifyArticle(
                        "nurban",
                        nurbanToken,
                        article.id,
                        thumbnailUrl,
                        binding.textEditorNurbanHeader.textEditorTitleEt.text.toString(),
                        (binding.textEditorNurbanLossCutEt.text.toString()).toLong(),
                        mEditor.html.toString()
                    )
                } else {
                    viewModel.uploadArticle(
                        "nurban",
                        nurbanToken,
                        binding.textEditorNurbanHeader.textEditorTitleEt.text.toString(),
                        uuid.toString(),
                        binding.textEditorNurbanLossCutEt.text.toString().toLong(),
                        thumbnailUrl,
                        mEditor.html.toString()
                    )
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}