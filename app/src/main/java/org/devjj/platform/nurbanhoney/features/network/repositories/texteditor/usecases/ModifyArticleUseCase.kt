package org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.TextEditorRepository
import org.devjj.platform.nurbanhoney.features.ui.textedit.ArticleResponse
import javax.inject.Inject

class ModifyArticleUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<ArticleResponse, ModifyArticleUseCase.Params>() {
    override suspend fun run(params: Params) = repository.modifyArticle(
        params.board,
        params.token,
        params.articleId,
        params.thumbnail,
        params.title,
        params.lossCut,
        params.content
    )

    data class Params(
        val board: String,
        val token: String,
        val articleId: Int,
        val thumbnail: String,
        val title: String,
        val lossCut: Long,
        val content: String
    )
}