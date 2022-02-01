package org.devjj.platform.nurbanhoney.features.network.repositories.rank.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.rank.RankRepository
import org.devjj.platform.nurbanhoney.features.ui.home.ranking.RankSimple
import javax.inject.Inject

class GetTopRanksUseCase
@Inject constructor(
    private val repository: RankRepository
) : UseCase<List<RankSimple>, GetTopRanksUseCase.Params>() {

    override suspend fun run(params: Params) = repository.getTopRanks(params.offset, params.limit)

    data class Params(
        val offset: Int,
        val limit: Int
    )
}