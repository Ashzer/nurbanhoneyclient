package org.devjj.platform.nurbanhoney.features.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.devjj.platform.nurbanhoney.features.ui.textedit.UploadImageEntity
import org.devjj.platform.nurbanhoney.features.ui.textedit.UploadResultEntity
import retrofit2.Call
import retrofit2.http.*

internal interface BoardApi {

    companion object {
        private const val UPLOAD = "nurbanboard"
        private const val UPLOAD_IMG = "nurbanboard/upload/image"
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
}