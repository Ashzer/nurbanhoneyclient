package org.devjj.platform.nurbanhoney.features.ui.article

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class GetCommentUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<Comment , GetCommentUseCase.Params>() {
    override suspend fun run(params: Params) = repository.getComment(params.commentId)

    data class Params(val commentId : Int)
}