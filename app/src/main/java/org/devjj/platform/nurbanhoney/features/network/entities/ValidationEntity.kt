package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.login.TokenStatus

data class ValidationEntity(
    @SerializedName("result")
    val isValid: Boolean?,
    @SerializedName("error")
    val error: String
) {
    companion object {
        val empty = ValidationEntity(null, "")
    }

    fun toIsTokenValid() = TokenStatus(isValid)
}