package org.devjj.platform.nurbanhoney.features.ui.article

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.BoardService
import org.devjj.platform.nurbanhoney.features.network.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.ui.home.ArticleEntity
import javax.inject.Inject

interface ArticleRepository {

    fun getArticle(token: String, id: Int): Either<Failure, Article>

    fun postLike(token: String, id: Int): Either<Failure, LikeResult>
    fun postDislike(token: String, id: Int): Either<Failure, LikeResult>
    fun postComment(token: String, comment: String, id: Int): Either<Failure, CommentResponse>
    fun getComments(articleId: Int, offset: Int, limit: Int): Either<Failure, List<Comment>>
    fun deleteComment(token: String, id: Int): Either<Failure, CommentResponse>
    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val boardService: BoardService
    ) : ArticleRepository {
        override fun getArticle(token: String, id: Int): Either<Failure, Article> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.getArticle(token, id),
                    { it.toArticle() },
                    ArticleEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun postLike(token: String, id: Int): Either<Failure, LikeResult> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.postLike(token, id),
                    { it.toLikeResult() },
                    ActionResultEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun postDislike(token: String, id: Int): Either<Failure, LikeResult> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.postDislike(token, id),
                    { it.toLikeResult() },
                    ActionResultEntity.empty
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
                    boardService.postComment(token, comment, id),
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
                    boardService.getComments(articleId, offset, limit),
                    { it.map { CommentEntity -> CommentEntity.toComment() } },
                    listOf(CommentEntity.empty)
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun deleteComment(token: String, id: Int): Either<Failure,CommentResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.deleteComment(token, id),
                    { it.toCommentResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
    }
}