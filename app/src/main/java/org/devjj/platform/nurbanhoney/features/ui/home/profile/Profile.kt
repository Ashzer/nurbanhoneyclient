package org.devjj.platform.nurbanhoney.features.ui.home.profile

import org.devjj.platform.nurbanhoney.core.extension.empty

data class Profile(
    val id: Int,
    val loginType: String,
    val badge: String?,
    val nickname: String,
    val description: String?,
    val point: Int,
    val insigniaShow: List<String>?,
    val insigniaOwn: List<String>?,
    val myArticleCount: Int,
    val myCommentCount: Int,
    val error: String?,
) {
    companion object {
        var empty = Profile(
            0,
            String.empty(),
            String.empty(),
            String.empty(),
            String.empty(),
            0,
            listOf(),
            listOf(),
            0,
            0,
            String.empty()
        )
    }
}
