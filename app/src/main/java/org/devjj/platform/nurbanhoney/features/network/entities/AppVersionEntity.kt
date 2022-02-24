package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName

data class AppVersionEntity(
    @SerializedName("appversion")
    val version: String,
    @SerializedName("isUpdate")
    val isUpdate: Boolean
)