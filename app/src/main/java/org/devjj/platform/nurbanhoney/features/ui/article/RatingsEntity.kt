package org.devjj.platform.nurbanhoney.features.ui.article

import com.google.gson.annotations.SerializedName

data class RatingsEntity(
    @SerializedName("likeCount") val likes: Int,
    @SerializedName("dislikeCount") val dislikes: Int,
    @SerializedName("myRating") val myRating: String?
) {
    fun toRatings() = Ratings(likes, dislikes, myRating?:"")

    companion object {
        val empty = RatingsEntity(-1, -1, "")
    }
}