package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.textedit.ImageUploadResult
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