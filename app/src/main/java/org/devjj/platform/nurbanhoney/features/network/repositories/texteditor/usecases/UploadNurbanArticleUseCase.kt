package org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.TextEditorRepository
import org.devjj.platform.nurbanhoney.features.ui.textedit.ArticleResponse
import javax.inject.Inject

class UploadNurbanArticleUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<ArticleResponse, UploadNurbanArticleUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.uploadNurbanArticle(
            params.board,
            params.token,
            params.title,
            params.uuid,
            params.lossCut,
            params.thumbnail,
            params.content
        )

    data class Params(
        val board: String,
        val token: String,
        val title: String,
        val uuid: String,
        val lossCut: Long,
        val thumbnail: String,
        val content: String,
    )
} //token,title, uuid , lossCut , thumbnail  ,content