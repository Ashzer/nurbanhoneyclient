package org.devjj.platform.nurbanhoney.features.network

import android.content.SharedPreferences
import org.devjj.platform.nurbanhoney.features.ui.textedit.UploadResultEntity
import retrofit2.Call
import retrofit2.http.*
import javax.inject.Inject

internal interface BoardApi{

    companion object{
        private const val UPLOAD = "nurbanboard"
        private const val UPLOAD_IMG = "nurbanboard/upload/image"
    }

    @FormUrlEncoded
    @POST(UPLOAD)
    fun uploadRequest(
        @Header("token") token : String,
        @Field("title") title : String,
        @Field("content") content : String
    ) : Call<UploadResultEntity>

    /*
    @Multipart
    @POST(UPLOAD_IMG)
    fun uploadImage(
        @Header("token") token : String//,
        //@Part("id") articleId
    )*/



}