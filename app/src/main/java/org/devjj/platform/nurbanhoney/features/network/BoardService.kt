package org.devjj.platform.nurbanhoney.features.network

import okhttp3.MultipartBody
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardService
@Inject constructor(retrofit: Retrofit) : BoardApi {
    private val boardApi by lazy { retrofit.create(BoardApi::class.java) }

    override fun uploadRequest(
        token: String,
        title: String,
        uuid: String,
        lossCut: Long,
        thumbnail: String,
        content: String
    ) =
        boardApi.uploadRequest(token, title, uuid, lossCut, thumbnail, content)

    override fun uploadImage(
        token: String,
        uuid: MultipartBody.Part,
        image: MultipartBody.Part
    ) = boardApi.uploadImage(token, uuid, image)

    override fun deleteImage(token: String, uuid: String) = boardApi.deleteImage(token, uuid)

    override fun getArticles(
        token: String,
        flag: Int,
        offset: Int,
        limit: Int
    ) = boardApi.getArticles(token, flag, offset, limit)

    override fun getArticle(token: String, id: Int) = boardApi.getArticle(token, id)

    override fun postLike(token: String, id: Int) = boardApi.postLike(token, id)

    override fun postDislike(token: String, id: Int) = boardApi.postDislike(token, id)

    override fun postComment(token: String, comment: String, id: Int) =
        boardApi.postComment(token, comment, id)

    override fun getComments(id: Int, offset: Int, limit: Int) =
        boardApi.getComments(id, offset, limit)

    override fun deleteComment(token: String, id: Int) = boardApi.deleteComment(token, id)
}