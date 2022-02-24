package org.devjj.platform.nurbanhoney.features.network.repositories.profile

import org.devjj.platform.nurbanhoney.features.network.entities.ProfileArticleEntity
import org.devjj.platform.nurbanhoney.features.network.entities.ProfileCommentEntity
import org.devjj.platform.nurbanhoney.features.network.entities.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.network.entities.ProfileEntity
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

    @GET(PROFILE_MYARTCIEL)
    fun getMyArticles(
        @Header("token") token: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Call<List<ProfileArticleEntity>>

    @GET(PROFILE_MYCOMMENT)
    fun getMyComments(
        @Header("token") token: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ) : Call<List<ProfileCommentEntity>>

    @DELETE(PROFILE_WITHDRAWAL)
    fun signOut(
        @Header("token") token: String,
        @Query("id") id: Int,
    ): Call<SimpleResponseEntity>


    @FormUrlEncoded
    @PATCH(PROFILE_EDIT)
    fun editProfile(
        @Header("token") token: String,
        @Field("nickname") nickname: String,
        @Field("description") description: String,
        @Field("insignia") insignia: List<String>,
    ) : Call<SimpleResponseEntity>
    /*POST
    @FormUrlEncoded
    @POST(ARTICLE_LIKE)
    fun postLike(
        @Header("token") token: String,
        @Field("articleId") id: Int
    ): Call<SimpleResponseEntity>
*/
    /*DELETE
    @DELETE(ARTICLE_LIKE)
    fun cancelLike(
        @Header("token") token: String,
        @Query("articleId") id: Int
    ): Call<SimpleResponseEntity>
*/
    /*GET
    @GET(ARTICLE_RATING)
    fun getRatings(
        @Header("token") token: String,
        @Query("articleId") articleId: Int
    ): Call<RatingsEntity>
*/
    /*PATCH
    @FormUrlEncoded
    @PATCH(ARTICLE_COMMENTS)
    fun updateComment(
        @Header("token") token: String,
        @Field("id") id: Int,
        @Field("content") content: String
    ): Call<SimpleResponseEntity>

    */
}