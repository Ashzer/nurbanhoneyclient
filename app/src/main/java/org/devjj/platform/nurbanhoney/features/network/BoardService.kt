package org.devjj.platform.nurbanhoney.features.network

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.features.ui.home.ArticlesRequestEntity
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardService
@Inject constructor(retrofit: Retrofit) : BoardApi {
    private val boardApi by lazy { retrofit.create(BoardApi::class.java) }

    override fun uploadRequest(token: String,title: String, uuid: String, lossCut : Long , thumbnail : String ,content: String ) =
        boardApi.uploadRequest(token,title, uuid , lossCut , thumbnail  ,content )

    override fun uploadImage(
        token: String,
        uuid: MultipartBody.Part,
        image: MultipartBody.Part
    ) = boardApi.uploadImage(token, uuid, image)

    override fun getArticles(
        token: String,
        flag : Int,
        offset: Int,
        limit: Int
    ) = boardApi.getArticles(token, flag, offset, limit)
}