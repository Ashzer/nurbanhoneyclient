package org.devjj.platform.nurbanhoney.features.network

import okhttp3.MultipartBody
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardService
@Inject constructor(retrofit: Retrofit) : BoardApi {
    private val boardApi by lazy { retrofit.create(BoardApi::class.java) }

    override fun getBoards() = boardApi.getBoards()

    override fun uploadNurbanRequest(
        board: String,
        token: String,
        title: String,
        uuid: String,
        lossCut: Long,
        thumbnail: String,
        content: String
    ) = boardApi.uploadNurbanRequest(board, token, title, uuid, lossCut, thumbnail, content)

    override fun uploadRequest(
        board: String,
        token: String,
        title: String,
        uuid: String,
        thumbnail: String,
        content: String
    ) = boardApi.uploadRequest(board, token, title, uuid, thumbnail, content)

    override fun modifyNurbanRequest(
        board: String,
        token: String,
        articleId: Int,
        thumbnail: String,
        title: String,
        lossCut: Long,
        content: String
    ) = boardApi.modifyNurbanRequest(board, token, articleId, thumbnail, title, lossCut, content)

    override fun modifyRequest(
        board: String,
        token: String,
        articleId: Int,
        thumbnail: String,
        title: String,
        content: String
    ) = boardApi.modifyRequest(board, token, articleId, thumbnail, title, content)

    override fun uploadImage(
        board: String,
        token: String,
        uuid: MultipartBody.Part,
        image: MultipartBody.Part
    ) = boardApi.uploadImage(board, token, uuid, image)

    override fun deleteImage(board: String, token: String, uuid: String) =
        boardApi.deleteImage(board, token, uuid)


    override fun deleteArticle(board: String, token: String, articleId: Int, uuid: String) =
        boardApi.deleteArticle(board, token, articleId, uuid)
}