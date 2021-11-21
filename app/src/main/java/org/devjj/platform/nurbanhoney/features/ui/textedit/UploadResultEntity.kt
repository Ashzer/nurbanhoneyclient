package org.devjj.platform.nurbanhoney.features.ui.textedit

import com.google.gson.annotations.SerializedName

data class UploadResultEntity(
    @SerializedName("result")
    val isUploadSucceed: String?,
    @SerializedName("error")
    val error: String?
) {

    fun toUploadResult() = UploadResult(isUploadSucceed, error)

    companion object {
        val empty = UploadResultEntity(null, null)
    }
}