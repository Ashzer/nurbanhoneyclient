package org.devjj.platform.nurbanhoney.features.network

import org.devjj.platform.nurbanhoney.features.ui.article.CommentEntity
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleService
@Inject constructor(retrofit: Retrofit) : ArticleApi {
    private val articleApi by lazy { retrofit.create(ArticleApi::class.java) }

    override fun getArticle(token: String, id: Int) = articleApi.getArticle(token, id)

    override fun postLike(token: String, id: Int) = articleApi.postLike(token, id)
    override fun cancelLike(token: String, id: Int) = articleApi.cancelLike(token, id)
    override fun postDislike(token: String, id: Int) = articleApi.postDislike(token, id)
    override fun cancelDislike(token: String, id: Int) = articleApi.cancelDislike(token, id)

    override fun getRatings(token: String, articleId: Int) = articleApi.getRatings(token, articleId)

    override fun postComment(token: String, comment: String, id: Int) =
        articleApi.postComment(token, comment, id)

    override fun getComments(id: Int, offset: Int, limit: Int) =
        articleApi.getComments(id, offset, limit)

    override fun deleteComment(token: String, id: Int, articleId: Int) =
        articleApi.deleteComment(token, id, articleId)

    override fun updateComment(token: String, id: Int, content: String) =
        articleApi.updateComment(token, id, content)

    override fun getComment(commentId: Int): Call<CommentEntity> = articleApi.getComment(commentId)
}