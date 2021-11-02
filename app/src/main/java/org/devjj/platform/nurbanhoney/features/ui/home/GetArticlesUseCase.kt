package org.devjj.platform.nurbanhoney.features.ui.home

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.ui.textedit.TextEditorRepository
import javax.inject.Inject

class GetArticlesUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<List<NurbanHoneyArticle>, GetArticlesUseCase.Params>() {
    override suspend fun run(params: Params) = repository.getArticles(params.token, params.flag, params.offset, params.limit)

    data class Params(val token: String, val flag : Int, val offset: Int, val limit: Int)
}