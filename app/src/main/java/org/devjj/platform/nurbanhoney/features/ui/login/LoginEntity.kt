package org.devjj.platform.nurbanhoney.features.ui.login

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.core.extension.*

data class LoginEntity(@SerializedName("login_result") var loginResult : LoginResult  ) {
    data class LoginResult(
        @SerializedName("token") var token : String,
        @SerializedName("error") var error : String?
    )
    companion object {
        val empty = LoginEntity(
            LoginResult("","")
        )
    }
    fun toNurbanToken() = NurbanToken(loginResult.token , loginResult.error ?: "")
}

