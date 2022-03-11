package org.devjj.platform.nurbanhoney.features.ui.home.profile

import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.features.Board
import org.threeten.bp.LocalDateTime

data class ProfileArticle(
    val id: Int,
    var board: Board,
    val thumbnail: String,
    val title: String,
    val commentCount: Int,
    val createAt: LocalDateTime?
) {
    companion object {
        val empty =
            ProfileArticle(0, Board.empty, String.empty(), String.empty(), 0, null)
    }
}