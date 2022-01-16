package org.devjj.platform.nurbanhoney.features.ui.home.profile

data class ProfileArticle(
    val id : Int,
    val flag : Int,
    val thumbnail : String,
    val title : String,
    val commentCount : Int,
    val createAt : String
)