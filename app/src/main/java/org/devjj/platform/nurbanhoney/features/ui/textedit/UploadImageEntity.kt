package org.devjj.platform.nurbanhoney.features.ui.textedit

import com.google.gson.annotations.SerializedName
import java.net.URL

class UploadImageEntity(
    @SerializedName("nurbanboard_image_result")
    val result : Result?
){
    data class Result(
        @SerializedName("result")
        val url: URL?,
        @SerializedName("error")
        val error : String?
    )

    fun toImageUploadResult() = ImageUploadResult(result?.url ?: URL("") , result?.error ?: "" )
    companion object {
        val empty = UploadImageEntity(
            UploadImageEntity.Result(null, null)
        )
    }
}