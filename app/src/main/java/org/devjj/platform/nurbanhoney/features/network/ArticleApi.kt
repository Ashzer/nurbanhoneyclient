package org.devjj.platform.nurbanhoney.features.network

import org.devjj.platform.nurbanhoney.features.ui.article.CommentEntity
import org.devjj.platform.nurbanhoney.features.ui.article.RatingsEntity
import org.devjj.platform.nurbanhoney.features.ui.home.ArticleEntity
import org.devjj.platform.nurbanhoney.features.ui.home.ArticlesRequestEntity
import retrofit2.Call
import retrofit2.http.*

internal interface ArticleApi {
    companion object {
        private const val NURBAN_LIST = "nurban"
        private const val ARTICLE_LIKE = "nurban/detail/like"
        private const val ARTICLE_DISLIKE = "nurban/detail/dislike"
        private const val ARTICLE_COMMENTS = "nurban/comment"
        private const val ARTICLE_COMMENT = "nurban/comment/detail"
        private const val NURBAN_ARTICLE = "nurban/detail"
        private const val ARTICLE_RATING = "nurban/myrating"
    }

    @GET(NURBAN_LIST)
    fun getArticles(
        @Query("flag") flag: Int = 0,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<List<ArticlesRequestEntity>>

    @GET(NURBAN_ARTICLE)
    fun getArticle(
        @Header("token") token: String,
        @Query("id") id: Int
    ): Call<ArticleEntity>

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

    @FormUrlEncoded
    @POST(ARTICLE_DISLIKE)
    fun postDislike(
        @Header("token") token: String,
        @Field("articleId") id: Int
    ): Call<SimpleResponseEntity>

    @DELETE(ARTICLE_DISLIKE)
    fun cancelDislike(
        @Header("token") token: String,
        @Query("articleId") id: Int
    ): Call<SimpleResponseEntity>

    @GET(ARTICLE_RATING)
    fun getRatings(
        @Header("token") token: String,
        @Query("articleId") articleId: Int
    ): Call<RatingsEntity>

    @FormUrlEncoded
    @POST(ARTICLE_COMMENTS)
    fun postComment(
        @Header("token") token: String,
        @Field("content") comment: String,
        @Field("articleId") id: Int
    ): Call<SimpleResponseEntity>

    @GET(ARTICLE_COMMENTS)
    fun getComments(
        @Query("articleId") id: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<List<CommentEntity>>

    @DELETE(ARTICLE_COMMENTS)
    fun deleteComment(
        @Header("token") token: String,
        @Query("id") id: Int,
        @Query("articleId") articleId: Int
    ): Call<SimpleResponseEntity>

    @FormUrlEncoded
    @PATCH(ARTICLE_COMMENTS)
    fun updateComment(
        @Header("token") token: String,
        @Field("id") id: Int,
        @Field("content") content: String
    ): Call<SimpleResponseEntity>

    @GET(ARTICLE_COMMENT)
    fun getComment(
        @Query("commentId") commentId: Int
    ): Call<CommentEntity>
}