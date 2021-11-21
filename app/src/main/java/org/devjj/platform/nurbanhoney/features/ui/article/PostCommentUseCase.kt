package org.devjj.platform.nurbanhoney.features.ui.article

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class PostCommentUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<CommentResponse, PostCommentUseCase.Params>(){
    override suspend fun run(params: Params) = repository.postComment(params.token, params.comment, params.id)

    data class Params(
        val token : String,
        val comment : String,
        val id : Int
    )
}