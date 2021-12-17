package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.Comment
import javax.inject.Inject

class GetCommentUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<Comment, GetCommentUseCase.Params>() {
    override suspend fun run(params: Params) = repository.getComment(params.board, params.commentId)

    data class Params(val board: String, val commentId: Int)
}