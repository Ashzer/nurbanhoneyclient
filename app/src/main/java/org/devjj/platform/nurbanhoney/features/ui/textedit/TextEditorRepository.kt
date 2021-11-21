package org.devjj.platform.nurbanhoney.features.ui.textedit

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.exception.Failure.NetworkConnection
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.BoardService
import org.devjj.platform.nurbanhoney.features.ui.home.NurbanHoneyArticle
import javax.inject.Inject

interface TextEditorRepository {
    fun uploadArticle(
        token: String,
        title: String,
        uuid: String,
        lossCut: Long,
        thumbnail: String,
        content: String
    ): Either<Failure, UploadResult>

    fun uploadImage(
        token: String,
        uuid: MultipartBody.Part,
        image: MultipartBody.Part
    ): Either<Failure, ImageUploadResult>

    fun getArticles(
        token: String,
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
        ): Either<Failure, UploadResult> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.uploadRequest(token, title, uuid, lossCut, thumbnail, content),
                    { it.toUploadResult() },
                    UploadResultEntity.empty
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
            token: String,
            flag: Int,
            offset: Int,
            limit: Int
        ): Either<Failure, List<NurbanHoneyArticle>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.getArticles(token, flag, offset, limit),
                    { it.map { ArticlesRequestEntity -> ArticlesRequestEntity.toNurbanHoneyArticle() } },
                    emptyList()
                )
                false -> Either.Left(NetworkConnection)
            }
        }


    }
}