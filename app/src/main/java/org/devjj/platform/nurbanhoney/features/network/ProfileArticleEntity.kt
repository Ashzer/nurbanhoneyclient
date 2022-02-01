package org.devjj.platform.nurbanhoney.features.network

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileArticle

data class ProfileArticleEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("flag") val flag: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("title") val title: String,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("createdAt") val createAt: String
) {
    companion object {
        val empty = ProfileArticleEntity(-1, "", String.empty(), String.empty(), -1, String.empty())
    }

    fun toProfileArticle() = ProfileArticle(id, flag, thumbnail, title, commentCount, createAt)
}