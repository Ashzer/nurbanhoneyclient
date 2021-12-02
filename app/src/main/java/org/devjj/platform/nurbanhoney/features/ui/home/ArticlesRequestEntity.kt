package org.devjj.platform.nurbanhoney.features.ui.home

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney.NurbanHoneyArticle

data class ArticlesRequestEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("title") val title: String,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("User") val user: User?
) {
    data class User(
        @SerializedName("userId") val userId: Int,
        @SerializedName("profile") val profile: String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("insignia") val insignia: String?
        //@SerializedName("insignia") val insignia : List<String>
    )

    fun toNurbanHoneyArticle() = NurbanHoneyArticle(
        id,
        thumbnail ?: "",
        title,
        commentCount,
        user?.profile ?: "",
        user?.nickname ?: "Empty Nickname",
        user?.insignia ?: "No insignia"
    )
}