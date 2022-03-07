package org.devjj.platform.nurbanhoney.features.ui.login

data class NurbanToken(
    val token : String,
    val userId : Int,
    val error : String
)