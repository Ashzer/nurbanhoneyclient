package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.core.utils.LocalDateTimeUtils
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileArticle
import org.devjj.platform.nurbanhoney.features.Board

data class ProfileArticleEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("board") val board: Board,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("title") val title: String,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("createdAt") val createAt: String
) {
    companion object {
        val empty = ProfileArticleEntity(
            -1,
            Board.empty,
            String.empty(),
            String.empty(),
            -1,
            String.empty()
        )
    }

    fun toProfileArticle() = ProfileArticle(
        id,
        board,
        thumbnail,
        title,
        commentCount,
        LocalDateTimeUtils.parse(createAt)
    )
}