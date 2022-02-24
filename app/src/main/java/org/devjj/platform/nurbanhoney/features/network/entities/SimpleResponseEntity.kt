package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.article.model.CommentResponse
import org.devjj.platform.nurbanhoney.features.ui.article.model.RatingResponse
import org.devjj.platform.nurbanhoney.features.ui.home.profile.EditProfileResponse
import org.devjj.platform.nurbanhoney.features.ui.home.profile.SignOutResponse
import org.devjj.platform.nurbanhoney.features.ui.textedit.ArticleResponse
import org.devjj.platform.nurbanhoney.features.ui.textedit.ImageResponse

data class SimpleResponseEntity(
    @SerializedName("result") val result : String
){
    fun toCommentResponse() = CommentResponse(result)
    fun toRatingResponse() = RatingResponse(result)
    fun toArticleResponse() = ArticleResponse(result)
    fun toImageResponse() = ImageResponse(result)
    fun toEditProfileResponse() = EditProfileResponse(result)
    fun toSignOutResponse() = SignOutResponse(result)
    companion object {
        val empty = SimpleResponseEntity("")
    }
}