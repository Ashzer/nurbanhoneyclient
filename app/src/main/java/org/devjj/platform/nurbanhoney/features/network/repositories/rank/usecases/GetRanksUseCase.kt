package org.devjj.platform.nurbanhoney.features.network.repositories.rank.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.core.interactor.UseCase.None
import org.devjj.platform.nurbanhoney.features.network.repositories.rank.RankRepository
import org.devjj.platform.nurbanhoney.features.ui.home.ranking.Rank
import javax.inject.Inject

class GetRanksUseCase
@Inject constructor(
    private val repository: RankRepository
) : UseCase<List<Rank>, None>() {
    override suspend fun run(params: None) = repository.getRanks()
}