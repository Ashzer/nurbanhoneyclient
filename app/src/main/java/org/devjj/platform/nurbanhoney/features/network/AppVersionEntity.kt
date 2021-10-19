package org.devjj.platform.nurbanhoney.features.network

import com.google.gson.annotations.SerializedName

data class AppVersionEntity(@SerializedName("appversion_result") val result : AppVersionResult ) {
    data class AppVersionResult(
        @SerializedName("appversion")
        val version : String,
        @SerializedName("isUpdate")
        val isUpdate : Boolean,
        @SerializedName("error")
        val error : String?
    )
}

