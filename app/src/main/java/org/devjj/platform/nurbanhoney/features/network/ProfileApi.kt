package org.devjj.platform.nurbanhoney.features.network

import org.devjj.platform.nurbanhoney.features.ui.home.ProfileEntity
import retrofit2.Call
import retrofit2.http.*

internal interface ProfileApi {
    companion object {
        private const val PROFILE = "profile"
        private const val PROFILE_EDIT = "profile/edit"
        private const val PROFILE_MYARTCIEL = "profile/myarticle"
        private const val PROFILE_MYCOMMENT = "profile/mycomment"
        private const val PROFILE_WITHDRAWAL = "profile/withdrawal"
    }

    @GET(PROFILE)
    fun getProfile(
        @Header("token") token: String
    ): Call<ProfileEntity>

    /*
    @FormUrlEncoded
    @POST(ARTICLE_LIKE)
    fun postLike(
        @Header("token") token: String,
        @Field("articleId") id: Int
    ): Call<SimpleResponseEntity>

    @DELETE(ARTICLE_LIKE)
    fun cancelLike(
        @Header("token") token: String,
        @Query("articleId") id: Int
    ): Call<SimpleResponseEntity>

    @GET(ARTICLE_RATING)
    fun getRatings(
        @Header("token") token: String,
        @Query("articleId") articleId: Int
    ): Call<RatingsEntity>

    @FormUrlEncoded
    @PATCH(ARTICLE_COMMENTS)
    fun updateComment(
        @Header("token") token: String,
        @Field("id") id: Int,
        @Field("content") content: String
    ): Call<SimpleResponseEntity>

    */
}