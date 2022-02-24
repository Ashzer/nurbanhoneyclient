package org.devjj.platform.nurbanhoney.features.ui.article.model

data class Comment(
    val id: Int,
    val comment: String,
    val articleId: Int,
    val userId : Int,
    val badge : String,
    val nickname : String,
    val insignia : String
)