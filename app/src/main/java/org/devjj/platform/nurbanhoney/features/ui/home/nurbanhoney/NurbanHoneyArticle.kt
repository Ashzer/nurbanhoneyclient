package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

import org.devjj.platform.nurbanhoney.features.ui.splash.Board

data class NurbanHoneyArticle(
    val id: Int,
    val thumbnail: String,
    val title: String,
    val replies: Int,
    val board : Board,
    val badge: String,
    val author: String,
    val medals: String
    //val medals : List<String>
)