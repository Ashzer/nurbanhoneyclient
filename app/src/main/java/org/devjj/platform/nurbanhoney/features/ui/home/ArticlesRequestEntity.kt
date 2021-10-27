package org.devjj.platform.nurbanhoney.features.ui.home

import com.google.gson.annotations.SerializedName

data class ArticlesRequestEntity(
    //@SerializedName("id")
    val id: String,
    //@SerializedName("thumbnail")
    val thumbnail: String,
   //@SerializedName("title")
    val title: String,
    //@SerializedName("commentCount")
    val commentCount: Int,
    //@SerializedName("User")
    val User: UserInfo
) {

    data class UserInfo(
        //@SerializedName("userId")
        val userId: Int,
        //@SerializedName("profile")
        val profile: String,
        //@SerializedName("nickname")
        val nickname: String,
        //@SerializedName("insignia")
        val insignia: String
        //@SerializedName("insignia") val insignia : List<String>
    )


    fun toNurbanHoneyArticle() = NurbanHoneyArticle(
        thumbnail,
        title,
        commentCount,
        User.profile,
        User.nickname,
        User.insignia
    )
}