package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.CommentResponse
import javax.inject.Inject

class UpdateCommentUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<CommentResponse, UpdateCommentUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.updateComment(params.board, params.token, params.id, params.comment)

    data class Params(
        val board: String,
        val token: String,
        val id: Int,
        val comment: String
    )
}