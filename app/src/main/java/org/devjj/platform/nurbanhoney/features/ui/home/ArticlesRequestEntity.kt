package org.devjj.platform.nurbanhoney.features.ui.home

import com.google.gson.annotations.SerializedName

data class ArticlesRequestEntity(
    @SerializedName("nurbanboard_list_result") val article: Article
) {
    data class Article(
        @SerializedName("id") val id: String,
        @SerializedName("thumbnail") val thumbnail: String,
        @SerializedName("title") val title: String,
        @SerializedName("commentCount") val comments: Int,
        @SerializedName("User") val userInfo: User
    ) {
        data class User(
            @SerializedName("userId") val id: Int,
            @SerializedName("profile") val badge: String,
            @SerializedName("nickname") val nickname: String,
            @SerializedName("insignia") val insignia: String
            //@SerializedName("insignia") val insignia : List<String>
        )
    }


    fun toNurbanHoneyArticle() = NurbanHoneyArticle(
        article.thumbnail,
        article.title,
        article.comments,
        article.userInfo.badge,
        article.userInfo.nickname,
        article.userInfo.insignia
    )
}