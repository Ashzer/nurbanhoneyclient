package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.RatingResponse
import javax.inject.Inject

class LikeUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<RatingResponse, LikeUseCase.Params>() {
    override suspend fun run(params: Params) = repository.postLike(params.token, params.id)

    data class Params(
        val token: String,
        val id: Int
    )
}