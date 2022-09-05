package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import org.devjj.platform.nurbanhoney.features.Board

data class ArticlesRequestEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("title") val title: String,
    @SerializedName("count") val count : Int?,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("board") val board : Board?,
    @SerializedName("user") val user: User?,
    @SerializedName("createdAt") val createdAt :String,
    @SerializedName("likeCount") val likeCount : Int
) {
    data class User(
        @SerializedName("userId") val userId: Int,
        @SerializedName("badge") val profile: String,
        @SerializedName("nickname") val nickname: String,
        //@SerializedName("insignia") val insignia: String?
        //@SerializedName("insignia") val insignia : List<String>
    )

    fun toNurbanHoneyArticle() = ArticleItem(
        id,
        thumbnail ?: "",
        title,
        commentCount,
        board ?: Board.empty,
        user?.profile ?: "",
        user?.nickname ?: "Empty Nickname",
        likeCount,
        createdAt
        //user?.insignia ?: "No insignia"
    )
}