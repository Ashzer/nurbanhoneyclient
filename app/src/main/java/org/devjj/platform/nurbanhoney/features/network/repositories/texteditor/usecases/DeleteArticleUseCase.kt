package org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.TextEditorRepository
import org.devjj.platform.nurbanhoney.features.ui.textedit.ArticleResponse
import javax.inject.Inject

class DeleteArticleUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<ArticleResponse, DeleteArticleUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.deleteArticle(params.token, params.articleId, params.uuid)

    data class Params(
        val token: String,
        val articleId: Int,
        val uuid: String,
    )
}