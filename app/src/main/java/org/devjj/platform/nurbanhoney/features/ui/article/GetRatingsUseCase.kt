package org.devjj.platform.nurbanhoney.features.ui.article

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class GetRatingsUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<Ratings, GetRatingsUseCase.Params>() {
    override suspend fun run(params: Params) = repository.getRatings(params.token,params.articleId)

    data class Params(
        val token : String,
        val articleId: Int
    )
}