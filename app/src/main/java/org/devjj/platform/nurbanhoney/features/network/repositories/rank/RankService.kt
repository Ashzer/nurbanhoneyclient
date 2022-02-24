package org.devjj.platform.nurbanhoney.features.network.repositories.rank

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RankService
@Inject constructor(retrofit: Retrofit) : RankApi {
    private val rankApi by lazy{ retrofit.create(RankApi::class.java)}

    override fun getRanks() = rankApi.getRanks()
    override fun getRanksTopThree(offset: Int, limit: Int) = rankApi.getRanksTopThree(offset, limit)
}