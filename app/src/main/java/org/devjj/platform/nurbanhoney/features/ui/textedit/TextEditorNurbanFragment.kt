package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.invisible
import org.devjj.platform.nurbanhoney.core.extension.visible
import org.devjj.platform.nurbanhoney.features.ui.article.Article

class TextEditorNurbanFragment : TextEditorFragment() {
    companion object {
        private const val PARAM_ARTICLE = "param_article"

        fun toModify(article: Article) = TextEditorNurbanFragment().apply {
            arguments = bundleOf(PARAM_ARTICLE to article)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textEditorNurbanLossCutClo.visible()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.writing_done -> {

                val thumbnailUrl = textEditorViewModel.searchThumbnail(mEditor.html.toString())
                Log.d("match_check__", thumbnailUrl)
                if (isModify) {
                    textEditorViewModel.modifyArticle(
                        "nurban",
                        prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
                        article.id,
                        thumbnailUrl,
                        binding.textEditorNurbanHeader.textEditorTitleEt.text.toString(),
                        (binding.textEditorNurbanLossCutEt.text.toString()).toLong(),
                        mEditor.html.toString()
                    )
                } else {
                    textEditorViewModel.uploadArticle(
                        "nurban",
                        prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
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