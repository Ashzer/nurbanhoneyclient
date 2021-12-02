package org.devjj.platform.nurbanhoney.features.ui.textedit

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class UploadArticleUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<ArticleResponse, UploadArticleUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.uploadArticle(
            params.token,
            params.title,
            params.uuid,
            params.lossCut,
            params.thumbnail,
            params.content
        )

    data class Params(
        val token: String,
        val title: String,
        val uuid: String,
        val lossCut: Long,
        val thumbnail: String,
        val content: String,
    )
} //token,title, uuid , lossCut , thumbnail  ,content