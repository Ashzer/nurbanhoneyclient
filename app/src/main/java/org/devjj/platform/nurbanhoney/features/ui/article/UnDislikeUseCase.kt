package org.devjj.platform.nurbanhoney.features.ui.article

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class UnDislikeUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<RatingResponse, UnDislikeUseCase.Params>() {
    override suspend fun run(params: Params) = repository.cancelDislike(params.token, params.id)

    data class Params(
        val token: String,
        val id: Int
    )
}