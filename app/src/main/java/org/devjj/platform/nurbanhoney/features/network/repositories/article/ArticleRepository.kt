package org.devjj.platform.nurbanhoney.features.network.repositories.article

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.entities.CommentEntity
import org.devjj.platform.nurbanhoney.features.network.entities.RatingsEntity
import org.devjj.platform.nurbanhoney.features.network.entities.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.network.entities.ArticleEntity
import org.devjj.platform.nurbanhoney.features.ui.article.model.*
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import javax.inject.Inject

interface ArticleRepository {
    fun getArticle(board: String, token: String, id: Int): Either<Failure, Article>
    fun postLike(board: String, token: String, id: Int): Either<Failure, RatingResponse>
    fun cancelLike(board: String, token: String, id: Int): Either<Failure, RatingResponse>
    fun postDislike(board: String, token: String, id: Int): Either<Failure, RatingResponse>
    fun cancelDislike(board: String, token: String, id: Int): Either<Failure, RatingResponse>
    fun getRatings(board: String, token: String, articleId: Int): Either<Failure, Ratings>
    fun postComment(
        board: String,
        token: String,
        comment: String,
        id: Int
    ): Either<Failure, CommentResponse>

    fun getComments(
        board: String,
        articleId: Int,
        offset: Int,
        limit: Int
    ): Either<Failure, List<Comment>>

    fun deleteComment(
        board: String,
        token: String,
        id: Int,
        articleId: Int
    ): Either<Failure, CommentResponse>

    fun updateComment(
        board: String,
        token: String,
        id: Int,
        content: String
    ): Either<Failure, CommentResponse>

    fun getComment(board: String, commentId: Int): Either<Failure, Comment>
    fun getArticles(
        board: String,
        flag: Int,
        offset: Int,
        limit: Int
    ): Either<Failure, List<ArticleItem>>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val articleService: ArticleService
    ) : ArticleRepository {

        override fun getArticles(
            board: String,
            flag: Int,
            offset: Int,
            limit: Int
        ): Either<Failure, List<ArticleItem>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getArticles(board, flag, offset, limit),
                    {
//                        it.forEach { item->
//                            Log.d("badge_check__","??? ${item.user?.profile?:"empty"}")
//                        }
                        it.map { ArticlesRequestEntity ->
                            ArticlesRequestEntity.toNurbanHoneyArticle()
                        }
                    },
                    emptyList()
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getArticle(board: String, token: String, id: Int): Either<Failure, Article> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getArticle(board, token, id),
                    { it.toArticle() },
                    ArticleEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun postLike(
            board: String,
            token: String,
            id: Int
        ): Either<Failure, RatingResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.postLike(board, token, id),
                    { it.toRatingResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun cancelLike(
            board: String,
            token: String,
            id: Int
        ): Either<Failure, RatingResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.cancelLike(board, token, id),
                    { it.toRatingResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun postDislike(
            board: String,
            token: String,
            id: Int
        ): Either<Failure, RatingResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.postDislike(board, token, id),
                    { it.toRatingResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun cancelDislike(
            board: String,
            token: String,
            id: Int
        ): Either<Failure, RatingResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.cancelDislike(board, token, id),
                    { it.toRatingResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getRatings(
            board: String,
            token: String,
            articleId: Int
        ): Either<Failure, Ratings> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getRatings(board, token, articleId),
                    { it.toRatings() },
                    RatingsEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun postComment(
            board: String,
            token: String,
            comment: String,
            id: Int
        ): Either<Failure, CommentResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.postComment(board, token, comment, id),
                    { it.toCommentResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getComments(
            board: String,
            articleId: Int,
            offset: Int,
            limit: Int
        ): Either<Failure, List<Comment>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getComments(board, articleId, offset, limit),
                    { it.map { CommentEntity -> CommentEntity.toComment() } },
                    listOf(CommentEntity.empty)
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getComment(board: String, commentId: Int): Either<Failure, Comment> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.getComment(board, commentId),
                    { it.toComment() },
                    CommentEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun deleteComment(
            board: String,
            token: String,
            id: Int,
            articleId: Int
        ): Either<Failure, CommentResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.deleteComment(board, token, id, articleId),
                    { it.toCommentResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun updateComment(
            board: String,
            token: String,
            id: Int,
            content: String
        ): Either<Failure, CommentResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    articleService.updateComment(board, token, id, content),
                    { it.toCommentResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
    }
}