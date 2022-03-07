
package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.login.NurbanToken

data class LoginEntity(
    @SerializedName("token") var token: String,
    @SerializedName("userId") var userId: Int,
    @SerializedName("error") var error: String?
) {

    companion object {
        val empty = LoginEntity("", -1, "")
    }

    fun toNurbanToken() = NurbanToken(token, userId , error ?: "")
}