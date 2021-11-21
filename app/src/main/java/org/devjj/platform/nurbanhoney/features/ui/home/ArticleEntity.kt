package org.devjj.platform.nurbanhoney.features.ui.home

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.article.Article

data class ArticleEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("uuid") val uuid: String,
    //@SerializedName("thumbnail") val thumbnail : String?,
    @SerializedName("title") val title: String?,
    @SerializedName("lossCut") val lossCut: Int,
    @SerializedName("content") val content: String?,
    @SerializedName("count") val inquiries: Int,
    @SerializedName("commentCount") val comments: Int,
    @SerializedName("likeCount") val likes: Int,
    @SerializedName("dislikeCount") val dislikes: Int,
    @SerializedName("updatedAt") val date: String,
    @SerializedName("badge") val badge: String?,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("insignia") val insignia: String?,
    @SerializedName("myRating") val myRating: String?,
    @SerializedName("error") val error: String?,
) {

    fun toArticle() =
        Article(
            id,
            uuid,
            title ?: "",
            lossCut,
            content ?: "",
            inquiries,
            comments,
            likes,
            dislikes,
            badge ?: "",
            nickname,
            insignia ?: "",
            myRating ?: "",
            error ?: ""
        )

    companion object {
        val empty = ArticleEntity(-1, "", "", 0, "", 0, 0, 0, 0, "", "", "", "", "", "")
    }
}