package org.devjj.platform.nurbanhoney.features.ui.textedit

import com.google.gson.annotations.SerializedName
import java.net.URL

class UploadImageEntity(
    @SerializedName("result")
    val url: String?
){

    fun toImageUploadResult() = ImageUploadResult(URL(url))
    companion object {
        val empty = UploadImageEntity(
            null
        )
    }
}