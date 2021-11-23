package org.devjj.platform.nurbanhoney.features.ui.article

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class UpdateCommentUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<CommentResponse, UpdateCommentUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.updateComment(params.token, params.id, params.comment)

    data class Params(
        val token: String,
        val id: Int,
        val comment: String
    )
}