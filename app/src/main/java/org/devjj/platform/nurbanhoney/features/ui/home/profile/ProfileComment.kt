package org.devjj.platform.nurbanhoney.features.ui.home.profile

data class ProfileComment(
    val id: Int,
    val content: String,
    val articleId: Int,
    val createAt: String,
    var flag: String,
    val title: String
)