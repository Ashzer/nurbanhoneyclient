package org.devjj.platform.nurbanhoney.features.ui.textedit

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.exception.Failure.NetworkConnection
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.BoardService
import org.devjj.platform.nurbanhoney.features.network.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney.NurbanHoneyArticle
import javax.inject.Inject

interface TextEditorRepository {
    fun uploadArticle(
        token: String,
        title: String,
        uuid: String,
        lossCut: Long,
        thumbnail: String,
        content: String
    ): Either<Failure, ArticleResponse>

    fun deleteArticle(token: String, articleId : Int, uuid: String) : Either<Failure,ArticleResponse>

    fun uploadImage(
        token: String,
        uuid: MultipartBody.Part,
        image: MultipartBody.Part
    ): Either<Failure, ImageUploadResult>

    fun getArticles(
        flag: Int,
        offset: Int,
        limit: Int
    ): Either<Failure, List<NurbanHoneyArticle>>


    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val boardService: BoardService
    ) : TextEditorRepository {
        override fun uploadArticle(
            token: String,
            title: String,
            uuid: String,
            lossCut: Long,
            thumbnail: String,
            content: String,
        ): Either<Failure, ArticleResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.uploadRequest(token, title, uuid, lossCut, thumbnail, content),
                    { it.toArticleResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }
        }

        override fun deleteArticle(
            token: String,
            articleId: Int,
            uuid: String
        ): Either<Failure, ArticleResponse> {
            return when(networkHandler.isNetworkAvailable()){
                true -> networkHandler.request(
                    boardService.deleteArticle(token,articleId,uuid),
                    {it.toArticleResponse()},
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }
        }

        override fun uploadImage(
            token: String,
            uuid: MultipartBody.Part,
            image: MultipartBody.Part
        ): Either<Failure, ImageUploadResult> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.uploadImage(token, uuid, image),
                    { it.toImageUploadResult() },
                    UploadImageEntity.empty
                )
                false -> Either.Left(NetworkConnection)
            }

        }

        override fun getArticles(
            flag: Int,
            offset: Int,
            limit: Int
        ): Either<Failure, List<NurbanHoneyArticle>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.getArticles(flag, offset, limit),
                    { it.map { ArticlesRequestEntity -> ArticlesRequestEntity.toNurbanHoneyArticle() } },
                    emptyList()
                )
                false -> Either.Left(NetworkConnection)
            }
        }


    }
}