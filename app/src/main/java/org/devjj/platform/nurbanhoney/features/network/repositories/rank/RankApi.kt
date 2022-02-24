package org.devjj.platform.nurbanhoney.features.network.repositories.rank

import org.devjj.platform.nurbanhoney.features.network.entities.RankEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RankApi {
    companion object {
        private const val BASE_RANK = "/rank"
        private const val POPUP = "/popup"
    }

    @GET("$BASE_RANK")
    fun getRanks() : Call<List<RankEntity>>

    @GET("$BASE_RANK$POPUP")
    fun getRanksTopThree(
        @Query("offset") offset : Int,
        @Query("limit") limit : Int
    ) : Call<List<RankEntity>>
}