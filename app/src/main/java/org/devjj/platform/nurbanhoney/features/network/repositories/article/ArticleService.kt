package org.devjj.platform.nurbanhoney.features.network.repositories.article

import org.devjj.platform.nurbanhoney.features.network.entities.CommentEntity
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleService
@Inject constructor(retrofit: Retrofit) : ArticleApi {
    private val articleApi by lazy { retrofit.create(ArticleApi::class.java) }

    override fun getArticle(board: String, token: String, id: Int) =
        articleApi.getArticle(board, token, id)

    override fun getArticles(
        board: String,
        flag: Int,
        offset: Int,
        limit: Int
    ) = articleApi.getArticles(board, flag, offset, limit)

    override fun postLike(board: String, token: String, id: Int) =
        articleApi.postLike(board, token, id)

    override fun cancelLike(board: String, token: String, id: Int) =
        articleApi.cancelLike(board, token, id)

    override fun postDislike(board: String, token: String, id: Int) =
        articleApi.postDislike(board, token, id)

    override fun cancelDislike(board: String, token: String, id: Int) =
        articleApi.cancelDislike(board, token, id)

    override fun getRatings(board: String, token: String, articleId: Int) =
        articleApi.getRatings(board, token, articleId)

    override fun postComment(board: String, token: String, comment: String, id: Int) =
        articleApi.postComment(board, token, comment, id)

    override fun getComments(board: String, id: Int, offset: Int, limit: Int) =
        articleApi.getComments(board, id, offset, limit)

    override fun deleteComment(board: String, token: String, id: Int, articleId: Int) =
        articleApi.deleteComment(board, token, id, articleId)

    override fun updateComment(board: String, token: String, id: Int, comment: String) =
        articleApi.updateComment(board, token, id, comment)

    override fun getComment(board: String, commentId: Int): Call<CommentEntity> =
        articleApi.getComment(board, commentId)


}