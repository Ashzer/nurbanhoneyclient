package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.features.ui.home.profile.Profile

data class ProfileEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("loginType") val loginType: String,
    @SerializedName("badge") val badge: String?,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("description") val description: String?,
    @SerializedName("point") val point: Int,
    @SerializedName("insigniaShow") val insigniaShow: List<String>?,
    @SerializedName("insigniaOwn") val insigniaOwn: List<String>?,
    @SerializedName("myArticleCount") val myArticleCount: Int,
    @SerializedName("myCommentCount") val myCommentCount: Int,
    @SerializedName("error") val error: String?,
) {
    fun toProfile() =
        Profile(
            id,
            loginType,
            badge,
            nickname,
            description,
            point,
            insigniaShow,
            insigniaOwn,
            myArticleCount,
            myCommentCount,
            error
        )

    companion object {
        val empty = ProfileEntity(0, String.empty(), String.empty(), String.empty(), String.empty(),
            0, listOf(), listOf(), 0, 0, String.empty())
    }
}