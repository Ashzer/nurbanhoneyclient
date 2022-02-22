package org.devjj.platform.nurbanhoney.features.ui.home

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney.NurbanHoneyArticle
import org.devjj.platform.nurbanhoney.features.ui.splash.Board

data class ArticlesRequestEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("title") val title: String,
    @SerializedName("count") val count : Int?,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("board") val board : Board?,
    @SerializedName("User") val user: User?
) {
    data class User(
        @SerializedName("userId") val userId: Int,
        @SerializedName("badge") val profile: String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("insignia") val insignia: String?
        //@SerializedName("insignia") val insignia : List<String>
    )

    fun toNurbanHoneyArticle() = NurbanHoneyArticle(
        id,
        thumbnail ?: "",
        title,
        commentCount,
        board ?: Board.empty,
        user?.profile ?: "",
        user?.nickname ?: "Empty Nickname",
        user?.insignia ?: "No insignia"
    )
}