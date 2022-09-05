package org.devjj.platform.nurbanhoney.features.ui.home.boards.model

import org.devjj.platform.nurbanhoney.features.Board

data class ArticleItem(
    val id: Int,
    val thumbnail: String,
    val title: String,
    val replies: Int,
    val board: Board,
    val badge: String,
    val author: String,
    val likeCount: Int,
    val createdAt: String
    //val medals: String
    //val medals : List<String>
)