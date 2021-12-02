package org.devjj.platform.nurbanhoney.features.ui.article

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.ArticleService
import org.devjj.platform.nurbanhoney.features.network.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.ui.home.ArticleEntity
import javax.inject.Inject

interface ArticleRepository {

    fun getArticle(token: String, id: Int): Either<Failure, Article>
    fun postLike(token: String, id: Int): Either<Failure, RatingResponse>
    fun cancelLike(token: String, id: Int): Either<Failure, RatingResponse>
    fun postDislike(token: String, id: Int): Either<Failure, RatingResponse>
    fun cancelDislike(token: String, id: Int): Either<Failure, RatingResponse>
    fun getRatings(token: String, articleId: Int): Either<Failure, Ratings>
    fun postComment(token: String, comment: String, id: Int): Either<Failure, CommentResponse>
    fun getComments(articleId: Int, offset: Int, limit: Int): Either<Failure, List<Comment>>
    fun deleteComment(token: String, id: Int, articleId: Int): Either<Failure, CommentResponse>
    fun updateComment(token: String, id: Int, content: String): Either<Failure, CommentResponse>
    fun getComment(commentId: Int): Either<Failure, Comment>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val articleService: ArticleService
    ) : ArticleRepository {
        override fun getArticle(token: String, id: Int): Either<Failure, Article> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getArticle(token, id),
                    { it.toArticle() },
                    ArticleEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun postLike(token: String, id: Int): Either<Failure, RatingResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.postLike(token, id),
                    { it.toRatingResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun cancelLike(token: String, id: Int): Either<Failure, RatingResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.cancelLike(token, id),
                    { it.toRatingResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun postDislike(token: String, id: Int): Either<Failure, RatingResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.postDislike(token, id),
                    { it.toRatingResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun cancelDislike(token: String, id: Int): Either<Failure, RatingResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.cancelDislike(token, id),
                    { it.toRatingResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getRatings(token: String, articleId: Int): Either<Failure, Ratings> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getRatings(token ,articleId),
                    { it.toRatings() },
                    RatingsEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun postComment(
            token: String,
            comment: String,
            id: Int
        ): Either<Failure, CommentResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.postComment(token, comment, id),
                    { it.toCommentResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getComments(
            articleId: Int,
            offset: Int,
            limit: Int
        ): Either<Failure, List<Comment>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getComments(articleId, offset, limit),
                    { it.map { CommentEntity -> CommentEntity.toComment() } },
                    listOf(CommentEntity.empty)
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getComment(commentId: Int): Either<Failure, Comment> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getComment(commentId),
                    { it.toComment() },
                    CommentEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun deleteComment(
            token: String,
            id: Int,
            articleId: Int
        ): Either<Failure, CommentResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.deleteComment(token, id, articleId),
                    { it.toCommentResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun updateComment(
            token: String,
            id: Int,
            content: String
        ): Either<Failure, CommentResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.updateComment(token, id, content),
                    { it.toCommentResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
    }
}