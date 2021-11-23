package org.devjj.platform.nurbanhoney.features.network

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.features.ui.home.ArticleEntity
import org.devjj.platform.nurbanhoney.features.ui.home.ArticlesRequestEntity
import org.devjj.platform.nurbanhoney.features.ui.textedit.UploadImageEntity
import org.devjj.platform.nurbanhoney.features.ui.textedit.UploadResultEntity
import retrofit2.Call
import retrofit2.http.*

internal interface BoardApi {

    companion object {
        private const val NURBAN_LIST = "nurbanboard"
        private const val UPLOAD_IMG = "nurbanboard/upload/image"
    }

    @FormUrlEncoded
    @POST(NURBAN_LIST)
    fun uploadRequest(
        @Header("token") token: String,
        @Field("title") title: String,
        @Field("uuid") uuid: String,
        @Field("lossCut") lossCut: Long,
        @Field("thumbnail") thumbnail: String,
        @Field("content") content: String
    ): Call<UploadResultEntity>


    @Multipart
    @POST(UPLOAD_IMG)
    fun uploadImage(
        @Header("token") token: String,
        @Part uuid: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Call<UploadImageEntity>

    @DELETE(UPLOAD_IMG)
    fun deleteImage(
        @Header("token") token: String,
        @Query("uuid") uuid: String
    )

    @GET(NURBAN_LIST)
    fun getArticles(
        @Header("token") token: String,
        @Query("flag") flag: Int = 0,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<List<ArticlesRequestEntity>>



}