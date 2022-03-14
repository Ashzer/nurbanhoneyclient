package org.devjj.platform.nurbanhoney.features.ui.article.model

import org.devjj.platform.nurbanhoney.core.extension.empty

data class Ratings(val likes: Int, val dislikes: Int, val myRating: String) {
    companion object {
        val empty = Ratings(0, 0, String.empty())
        
        enum class MyRating(
            val LIKE: String = "like",
            val DISLIKE : String = "dislike",
            val NONE : String? = null
        )
    }
}