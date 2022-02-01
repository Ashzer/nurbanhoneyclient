package org.devjj.platform.nurbanhoney.features.network.repositories.rank

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.RankService
import org.devjj.platform.nurbanhoney.features.ui.home.ranking.Rank
import org.devjj.platform.nurbanhoney.features.ui.home.ranking.RankSimple
import javax.inject.Inject

interface RankRepository {
    fun getRanks(): Either<Failure, List<Rank>>
    fun getTopRanks(offset: Int, limit: Int): Either<Failure, List<RankSimple>>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val rankService: RankService
    ) : RankRepository {
        override fun getRanks(): Either<Failure, List<Rank>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    rankService.getRanks(),
                    { it.map { RankEntity -> RankEntity.toRank() } },
                    listOf()
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getTopRanks(offset: Int, limit: Int): Either<Failure, List<RankSimple>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    rankService.getRanksTopThree(offset, limit),
                    { it.map { RankEntity -> RankEntity.toRankSimple() } },
                    listOf()
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
    }
}