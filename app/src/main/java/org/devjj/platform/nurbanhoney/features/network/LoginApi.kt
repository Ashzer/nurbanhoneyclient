package org.devjj.platform.nurbanhoney.features.network

import org.devjj.platform.nurbanhoney.features.ui.login.LoginEntity
import org.devjj.platform.nurbanhoney.features.ui.login.ValidationEntity
import retrofit2.Call
import retrofit2.http.*

internal interface LoginApi {
    companion object{
        private const val LOGIN = "login"
        private const val VALIDATION = "token/exam"
    }

    @FormUrlEncoded
    @POST(LOGIN)
    fun loginRequest(
        @Field("loginType") type : String,
        @Field("key") key : String) : Call<LoginEntity>

    @FormUrlEncoded
    @POST(VALIDATION)
    fun validationCheck(@Field("token") token : String) : Call<ValidationEntity>
}