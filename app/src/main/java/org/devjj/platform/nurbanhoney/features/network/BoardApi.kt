package org.devjj.platform.nurbanhoney.features.network

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.features.ui.home.ArticlesRequestEntity
import org.devjj.platform.nurbanhoney.features.ui.textedit.UploadImageEntity
import retrofit2.Call
import retrofit2.http.*

internal interface BoardApi {

    companion object {
        private const val NURBAN_LIST = "nurban"
        private const val UPLOAD_IMG = "nurban/upload/image"
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
    ): Call<SimpleResponseEntity>

    @FormUrlEncoded
    @PATCH(NURBAN_LIST)
    fun modifyRequest(
        @Header("token") token : String,
        @Field("id") articleId : Int,
        @Field("thumbnail") thumbnail: String,
        @Field("title") title : String,
        @Field("lossCut") lossCut: Long,
        @Field("content") content :String
    ) : Call<SimpleResponseEntity>

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
    ) : Call<SimpleResponseEntity>

    @DELETE(NURBAN_LIST)
    fun deleteArticle(
        @Header("token") token: String,
        @Query("id") articleId: Int,
        @Query("uuid") uuid: String
    ): Call<SimpleResponseEntity>

}