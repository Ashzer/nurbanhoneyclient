package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileComment
import org.devjj.platform.nurbanhoney.features.Board

data class ProfileCommentEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("board") val board: Board,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createAt: String,
    @SerializedName("Board") val articleInfo: ArticleInfo,
) {
    data class ArticleInfo(
        @SerializedName("articleId") val articleId: Int,
        @SerializedName("title") val title: String,
    )

    companion object {
        val empty = ProfileCommentEntity(-1, Board.empty, String.empty(), String.empty(), ArticleInfo(-1,String.empty()))
    }

    fun toProfileComment() = ProfileComment(id, content, articleInfo.articleId, createAt, board, articleInfo.title)
}