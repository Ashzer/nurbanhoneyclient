package org.devjj.platform.nurbanhoney.features.ui.article.model

import org.devjj.platform.nurbanhoney.core.extension.empty

data class Comment(
    val id: Int,
    val comment: String,
    val articleId: Int,
    val userId: Int,
    val badge: String,
    val nickname: String,
    val insignia: List<String>
) {
    companion object {
        val empty = Comment(0,String.empty(), 0, 0, String.empty(), String.empty(), listOf())
    }
}