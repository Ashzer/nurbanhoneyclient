package org.devjj.platform.nurbanhoney.features.ui.home.profile

import org.devjj.platform.nurbanhoney.features.Board
import org.threeten.bp.LocalDateTime

data class ProfileComment(
    val id: Int,
    val content: String,
    val articleId: Int,
    val createAt: LocalDateTime?,
    var board: Board,
    val title: String
)