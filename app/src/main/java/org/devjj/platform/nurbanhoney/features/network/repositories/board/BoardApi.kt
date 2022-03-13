package org.devjj.platform.nurbanhoney.features.network.repositories.board

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.features.network.entities.BoardEntity
import org.devjj.platform.nurbanhoney.features.network.entities.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.network.entities.UploadImageEntity
import retrofit2.Call
import retrofit2.http.*

internal interface BoardApi {

    companion object {
        private const val BASE_BOARD = "/board"
        private const val ARTICLE = "/article"
        private const val UPLOAD_IMG = "/upload/image"
    }

    @GET("$BASE_BOARD")
    fun getBoards(): Call<List<BoardEntity>>


    @FormUrlEncoded
    @POST("$BASE_BOARD/{board}$ARTICLE")
    fun uploadNurbanRequest(
        @Path("board") board: String,
        @Header("token") token: String,
        @Field("title") title: String,
        @Field("uuid") uuid: String,
        @Field("lossCut") lossCut: Long,
        @Field("thumbnail") thumbnail: String?,
        @Field("content") content: String
    ): Call<SimpleResponseEntity>

    @FormUrlEncoded
    @POST("$BASE_BOARD/{board}$ARTICLE")
    fun uploadRequest(
        @Path("board") board: String,
        @Header("token") token: String,
        @Field("title") title: String,
        @Field("uuid") uuid: String,
        @Field("thumbnail") thumbnail: String?,
        @Field("content") content: String
    ): Call<SimpleResponseEntity>

    @FormUrlEncoded
    @PATCH("$BASE_BOARD/{board}$ARTICLE")
    fun modifyNurbanRequest(
        @Path("board") board: String,
        @Header("token") token: String,
        @Field("id") articleId: Int,
        @Field("thumbnail") thumbnail: String?,
        @Field("title") title: String,
        @Field("lossCut") lossCut: Long,
        @Field("content") content: String
    ): Call<SimpleResponseEntity>

    @FormUrlEncoded
    @PATCH("$BASE_BOARD/{board}$ARTICLE")
    fun modifyRequest(
        @Path("board") board: String,
        @Header("token") token: String,
        @Field("id") articleId: Int,
        @Field("thumbnail") thumbnail: String?,
        @Field("title") title: String,
        @Field("content") content: String
    ): Call<SimpleResponseEntity>

    @Multipart
    @POST("$BASE_BOARD/{board}$ARTICLE$UPLOAD_IMG")
    fun uploadImage(
        @Path("board") board: String,
        @Header("token") token: String,
        @Part uuid: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Call<UploadImageEntity>

    @DELETE("$BASE_BOARD/{board}$ARTICLE$UPLOAD_IMG")
    fun deleteImage(
        @Path("board") board: String,
        @Header("token") token: String,
        @Query("uuid") uuid: String
    ): Call<SimpleResponseEntity>

    @DELETE("$BASE_BOARD/{board}$ARTICLE")
    fun deleteArticle(
        @Path("board") board: String,
        @Header("token") token: String,
        @Query("id") articleId: Int,
        @Query("uuid") uuid: String
    ): Call<SimpleResponseEntity>

}