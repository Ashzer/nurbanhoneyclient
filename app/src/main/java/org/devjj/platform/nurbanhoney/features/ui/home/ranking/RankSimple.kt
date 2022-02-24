package org.devjj.platform.nurbanhoney.features.ui.home.ranking

data class RankSimple(
    val id: Int,
    val userId: Int,
    val badge: String,
    val nickname: String,
    val insignia: List<String>?
)