package org.devjj.platform.nurbanhoney.features.network

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.ui.login.IsTokenValid
import org.devjj.platform.nurbanhoney.features.ui.login.LoginEntity
import org.devjj.platform.nurbanhoney.features.ui.login.TokenValidation

data class ValidationEntity(
    @SerializedName("token_exaim_result")
    val result : Result
){
    data class Result(
        @SerializedName("result")
        val isValid : Boolean?,
        @SerializedName("error")
        val error : String
    )

    companion object {
        val empty = ValidationEntity(
            Result(null,"")
        )
    }

    fun toIsTokenValid() = TokenValidation(result.isValid)
}