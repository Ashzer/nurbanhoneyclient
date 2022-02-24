package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.Ratings
import javax.inject.Inject

class GetRatingsUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<Ratings, GetRatingsUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.getRatings(params.board, params.token, params.articleId)

    data class Params(
        val board: String,
        val token: String,
        val articleId: Int
    )
}