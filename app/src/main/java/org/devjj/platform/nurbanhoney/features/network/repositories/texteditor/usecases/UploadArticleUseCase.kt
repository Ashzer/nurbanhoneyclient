package org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.TextEditorRepository
import org.devjj.platform.nurbanhoney.features.ui.textedit.ArticleResponse
import javax.inject.Inject

class UploadArticleUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<ArticleResponse, UploadArticleUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.uploadArticle(
            params.board,
            params.token,
            params.title,
            params.uuid,
            params.thumbnail,
            params.content
        )

    data class Params(
        val board: String,
        val token: String,
        val title: String,
        val uuid: String,
        val thumbnail: String,
        val content: String,
    )
}