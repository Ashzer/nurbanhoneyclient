package org.devjj.platform.nurbanhoney.features.ui.article

data class Article(
    val id: Int,
    val uuid: String,
    //val thumbnail : String?,
    val title: String,
    val lossCut: Int,
    val content: String,
    val inquiries: Int,
    val comments: Int,
    val likes: Int,
    val dislikes: Int,
    //val date : String,
    val badge: String,
    val nickname: String,
    val insignia: String,
    val myRating: String,
    val error: String,
)