package org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileArticle
import javax.inject.Inject

class GetProfileArticlesUseCase
@Inject constructor(
    private val repository: ProfileRepository
) : UseCase<List<ProfileArticle>, GetProfileArticlesUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.getMyArticles(params.token, params.offset, params.limit)

    data class Params(val token: String, val offset: Int, val limit: Int)
}