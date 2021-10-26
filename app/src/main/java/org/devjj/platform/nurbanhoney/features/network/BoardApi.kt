package org.devjj.platform.nurbanhoney.features.network

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.features.ui.home.ArticlesRequestEntity
import org.devjj.platform.nurbanhoney.features.ui.textedit.UploadImageEntity
import org.devjj.platform.nurbanhoney.features.ui.textedit.UploadResultEntity
import retrofit2.Call
import retrofit2.http.*

internal interface BoardApi {

    companion object {
        private const val UPLOAD = "nurbanboard"
        private const val UPLOAD_IMG = "nurbanboard/upload/image"
        private const val GET_ARTICLE_LIST = "nurbanboard"
    }

    @FormUrlEncoded
    @POST(UPLOAD)
    fun uploadRequest(
        @Header("token") token: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("uuid") uuid: String
    ): Call<UploadResultEntity>


    @Multipart
    @POST(UPLOAD_IMG)
    fun uploadImage(
        @Header("token") token: String,
        @Part uuid: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Call<UploadImageEntity>

    @GET(GET_ARTICLE_LIST)
    fun getArticles(
        @Header("token") token: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<List<ArticlesRequestEntity>>
}