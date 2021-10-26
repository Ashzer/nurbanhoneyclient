package org.devjj.platform.nurbanhoney.features.ui.textedit

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class UploadArticleUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<UploadResult, UploadArticleUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.uploadArticle(params.token, params.title, params.content, params.uuid)

    data class Params(val token: String, val title: String, val content: String, val uuid: String)
}