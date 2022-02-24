package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.CommentResponse
import javax.inject.Inject

class PostCommentUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<CommentResponse, PostCommentUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.postComment(params.board, params.token, params.comment, params.id)

    data class Params(
        val board: String,
        val token: String,
        val comment: String,
        val id: Int
    )
}