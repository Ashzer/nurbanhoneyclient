package org.devjj.platform.nurbanhoney.features.ui.textedit

import com.google.gson.annotations.SerializedName

data class UploadResultEntity(
    @SerializedName("nurbanboard_create_result")
    val result: Result?
) {
    data class Result(
        @SerializedName("result")
        val isUploadSucceed: String?,
        @SerializedName("error")
        val error: String?
    )

    fun toUploadResult() = UploadResult(result?.isUploadSucceed, result?.error)

    companion object {
        val empty = UploadResultEntity(
            Result(null, null)
        )
    }
}