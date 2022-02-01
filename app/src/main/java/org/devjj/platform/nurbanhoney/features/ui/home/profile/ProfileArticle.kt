package org.devjj.platform.nurbanhoney.features.ui.home.profile

data class ProfileArticle(
    val id : Int,
    var flag : String,
    val thumbnail : String,
    val title : String,
    val commentCount : Int,
    val createAt : String
)