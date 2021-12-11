package org.devjj.platform.nurbanhoney.features.ui.home.profile

data class Profile(
    val id: Int,
    val loginType: String,
    val badge: String?,
    val nickname: String,
    val description: String?,
    val point: Int,
    val insigniaShow: String?,
    val insigniaOwn: String?,
    val myArticleCount: Int,
    val myCommentCount: Int,
    val error: String?,
)
