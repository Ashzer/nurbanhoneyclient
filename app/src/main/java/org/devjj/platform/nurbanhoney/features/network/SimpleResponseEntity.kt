package org.devjj.platform.nurbanhoney.features.network

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.article.CommentResponse

data class SimpleResponseEntity(
    @SerializedName("result") val result : String
){
    fun toCommentResponse() = CommentResponse(result)

    companion object {
        val empty = SimpleResponseEntity("")
    }
}