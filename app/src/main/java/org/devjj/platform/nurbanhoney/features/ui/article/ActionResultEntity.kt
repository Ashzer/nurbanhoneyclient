package org.devjj.platform.nurbanhoney.features.ui.article

import com.google.gson.annotations.SerializedName

data class ActionResultEntity(
    @SerializedName("likeCount") val likes: Int,
    @SerializedName("dislikeCount") val dislikes: Int
) {
    fun toLikeResult() = LikeResult(likes, dislikes)

    companion object {
        val empty = ActionResultEntity(-1, -1)
    }
}