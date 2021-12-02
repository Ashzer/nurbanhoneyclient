package org.devjj.platform.nurbanhoney.features.network

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.article.CommentResponse
import org.devjj.platform.nurbanhoney.features.ui.article.RatingResponse
import org.devjj.platform.nurbanhoney.features.ui.textedit.ArticleResponse

data class SimpleResponseEntity(
    @SerializedName("result") val result : String
){
    fun toCommentResponse() = CommentResponse(result)
    fun toRatingResponse() = RatingResponse(result)
    fun toArticleResponse() = ArticleResponse(result)
    companion object {
        val empty = SimpleResponseEntity("")
    }
}