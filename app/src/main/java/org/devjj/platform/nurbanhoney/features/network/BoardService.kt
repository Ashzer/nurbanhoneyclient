package org.devjj.platform.nurbanhoney.features.network

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardService
@Inject constructor(retrofit: Retrofit) : BoardApi {
    private val boardApi by lazy { retrofit.create(BoardApi::class.java) }

    override fun uploadRequest(token: String, title: String, content: String, uuid: String) =
        boardApi.uploadRequest(token, title, content, uuid)
}