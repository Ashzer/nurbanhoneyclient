package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.Comment
import javax.inject.Inject

class GetCommentsUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<List<Comment>, GetCommentsUseCase.Params>() {
    override suspend fun run(params: Params) = repository.getComments(params.articleId,params.offset,params.limit)

    data class Params(val articleId:Int, val offset : Int, val limit : Int)
}