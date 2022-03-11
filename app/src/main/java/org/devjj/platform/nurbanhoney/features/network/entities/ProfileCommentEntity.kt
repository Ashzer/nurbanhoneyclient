package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.core.utils.LocalDateTimeUtils
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileComment

data class ProfileCommentEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("board") val board: Board,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createAt: String?,
    @SerializedName("Location") val articleInfo: ArticleInfo,
) {
    data class ArticleInfo(
        @SerializedName("articleId") val articleId: Int,
        @SerializedName("title") val title: String,
    )

    companion object {
        val empty = ProfileCommentEntity(
            -1,
            Board.empty,
            String.empty(),
            null,
            ArticleInfo(-1, String.empty())
        )
    }

    fun toProfileComment() = ProfileComment(
        id,
        content,
        articleInfo.articleId,
        if(createAt.isNullOrEmpty()) null else LocalDateTimeUtils.parse(createAt),
        board,
        articleInfo.title
    )
}