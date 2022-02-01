package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

data class NurbanHoneyArticle(
    val id: Int,
    val thumbnail: String,
    val title: String,
    val replies: Int,
    val boardAddress : String?,
    val badge: String,
    val author: String,
    val medals: String
    //val medals : List<String>
)