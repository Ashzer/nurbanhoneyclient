package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.CommentResponse
import javax.inject.Inject

class DeleteCommentUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<CommentResponse, DeleteCommentUseCase.Params>() {
    override suspend fun run(params: Params) = repository.deleteComment(params.token, params.id, params.articleId)

    data class Params(
        val token : String,
        val id : Int,
        val articleId : Int
    )
}