package org.devjj.platform.nurbanhoney.features.network.repositories.texteditor

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.exception.Failure.NetworkConnection
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.repositories.board.BoardService
import org.devjj.platform.nurbanhoney.features.network.entities.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.ui.textedit.ArticleResponse
import org.devjj.platform.nurbanhoney.features.ui.textedit.ImageResponse
import org.devjj.platform.nurbanhoney.features.ui.textedit.ImageUploadResult
import org.devjj.platform.nurbanhoney.features.network.entities.UploadImageEntity
import javax.inject.Inject

interface TextEditorRepository {
    fun uploadNurbanArticle(
        board: String,
        token: String,
        title: String,
        uuid: String,
        lossCut: Long,
        thumbnail: String,
        content: String
    ): Either<Failure, ArticleResponse>

    fun uploadArticle(
        board: String,
        token: String,
        title: String,
        uuid: String,
        thumbnail: String?,
        content: String
    ): Either<Failure, ArticleResponse>

    fun modifyNurbanArticle(
        board: String,
        token: String,
        articleId: Int,
        thumbnail: String,
        title: String,
        lossCut: Long,
        content: String
    ): Either<Failure, ArticleResponse>

    fun modifyArticle(
        board: String,
        token: String,
        articleId: Int,
        thumbnail: String?,
        title: String,
        content: String
    ): Either<Failure, ArticleResponse>

    fun deleteArticle(
        board: String,
        token: String,
        articleId: Int,
        uuid: String
    ): Either<Failure, ArticleResponse>

    fun uploadImage(
        board: String,
        token: String,
        uuid: MultipartBody.Part,
        image: MultipartBody.Part
    ): Either<Failure, ImageUploadResult>

    fun deleteImages(board: String, token: String, uuid: String): Either<Failure, ImageResponse>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val boardService: BoardService
    ) : TextEditorRepository {
        override fun uploadNurbanArticle(
            board: String,
            token: String,
            title: String,
            uuid: String,
            lossCut: Long,
            thumbnail: String,
            content: String,
        ): Either<Failure, ArticleResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.uploadNurbanRequest(
                        board,
                        token,
                        title,
                        uuid,
                        lossCut,
                        thumbnail,
                        content
                    ),
                    { it.toArticleResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }
        }

        override fun uploadArticle(
            board: String,
            token: String,
            title: String,
            uuid: String,
            thumbnail: String?,
            content: String
        ): Either<Failure, ArticleResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.uploadRequest(
                        board,
                        token,
                        title,
                        uuid,
                        thumbnail,
                        content
                    ),
                    { it.toArticleResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }
        }

        override fun modifyNurbanArticle(
            board: String,
            token: String,
            articleId: Int,
            thumbnail: String,
            title: String,
            lossCut: Long,
            content: String
        ): Either<Failure, ArticleResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.modifyNurbanRequest(
                        board,
                        token,
                        articleId,
                        thumbnail,
                        title,
                        lossCut,
                        content
                    ),
                    { it.toArticleResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }
        }

        override fun modifyArticle(
            board: String,
            token: String,
            articleId: Int,
            thumbnail: String?,
            title: String,
            content: String
        ): Either<Failure, ArticleResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.modifyRequest(
                        board,
                        token,
                        articleId,
                        thumbnail,
                        title,
                        content
                    ),
                    { it.toArticleResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }
        }

        override fun deleteArticle(
            board: String,
            token: String,
            articleId: Int,
            uuid: String
        ): Either<Failure, ArticleResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.deleteArticle(board, token, articleId, uuid),
                    { it.toArticleResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }
        }

        override fun uploadImage(
            board: String,
            token: String,
            uuid: MultipartBody.Part,
            image: MultipartBody.Part
        ): Either<Failure, ImageUploadResult> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.uploadImage(board, token, uuid, image),
                    { it.toImageUploadResult() },
                    UploadImageEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }

        }

        override fun deleteImages(
            board: String,
            token: String,
            uuid: String
        ): Either<Failure, ImageResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.deleteImage(board, token, uuid),
                    { it.toImageResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }
        }
    }
}