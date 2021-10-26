package org.devjj.platform.nurbanhoney.features.ui.textedit

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.BoardService
import javax.inject.Inject

interface TextEditorRepository {
    fun uploadArticle(
        token: String,
        title: String,
        content: String,
        uuid: String
    ): Either<Failure, UploadResult>

    fun uploadImage(
        token: String,
        uuid: MultipartBody.Part,
        image: MultipartBody.Part
    ): Either<Failure, ImageUploadResult>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val boardService: BoardService
    ) : TextEditorRepository {
        override fun uploadArticle(
            token: String,
            title: String,
            content: String,
            uuid: String
        ): Either<Failure, UploadResult> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    boardService.uploadRequest(token, title, content, uuid),
                    { it.toUploadResult() },
                    UploadResultEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
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
                false -> Either.Left(Failure.NetworkConnection)
            }

        }
    }
}