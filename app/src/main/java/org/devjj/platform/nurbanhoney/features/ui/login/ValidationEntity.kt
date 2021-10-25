package org.devjj.platform.nurbanhoney.features.ui.login

import com.google.gson.annotations.SerializedName

data class ValidationEntity (
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

    fun toIsTokenValid() = TokenStatus(result.isValid)
}