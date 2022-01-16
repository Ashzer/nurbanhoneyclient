package org.devjj.platform.nurbanhoney.features.network

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileComment

data class ProfileCommentEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("flag") val flag: Int,
    @SerializedName("content") val content: String,
    @SerializedName("articleId") val articleId: Int,
    @SerializedName("createAt") val createAt: String,
    @SerializedName("Board") val board: Board,
) {
    data class Board(
        @SerializedName("articleId") val articleId: Int,
        @SerializedName("title") val title: String,
    )

    companion object {
        val empty = ProfileCommentEntity(-1, -1, String.empty(), -1, String.empty(),Board(-1,""))
    }

    fun toProfileComment() = ProfileComment(id, content, articleId, createAt)
}