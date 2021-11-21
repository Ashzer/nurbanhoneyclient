package org.devjj.platform.nurbanhoney.features.ui.article

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class GetCommentsUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<List<Comment>, GetCommentsUseCase.Params>() {
    override suspend fun run(params: Params) = repository.getComments(params.articleId,params.offset,params.limit)

    data class Params(val articleId:Int, val offset : Int, val limit : Int)
}