package org.devjj.platform.nurbanhoney.features.ui.home

data class NurbanHoneyArticle(
    val thumbnail : String,
    val title: String,
    val replies : Int,
    val badge : String,
    val author : String,
    val medals : String
    //val medals : List<String>
)