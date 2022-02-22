package org.devjj.platform.nurbanhoney.features.ui.home.profile

import org.devjj.platform.nurbanhoney.features.ui.splash.Board

data class ProfileComment(
    val id: Int,
    val content: String,
    val articleId: Int,
    val createAt: String,
    var board: Board,
    val title: String
)