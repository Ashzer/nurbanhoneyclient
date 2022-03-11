package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.article.model.Ratings

data class RatingsEntity(
    @SerializedName("likeCount") val likes: Int,
    @SerializedName("dislikeCount") val dislikes: Int,
    @SerializedName("myRating") val myRating: String?
) {
    fun toRatings() = Ratings(likes, dislikes, myRating ?: "")

    companion object {
        val empty = RatingsEntity(0, 0, "")
    }
}