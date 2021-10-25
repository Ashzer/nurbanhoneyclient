package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.*
import org.devjj.platform.nurbanhoney.features.network.LoginService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Authenticator
@Inject constructor(
    val loginService: LoginService,
    val prefs: SharedPreferences
){
    suspend fun userLoggedIn() :Boolean = CoroutineScope(Dispatchers.IO).async {
        Log.d("token_check__","${prefs.getString("NurbanToken", "").toString()} by sharedPreference")
        return@async loginService.validationCheck(prefs.getString("NurbanToken", "").orEmpty()).execute().body()?.result?.isValid?:false
    }.await()
/*
    suspend fun userLoggedIn() :Boolean = CoroutineScope(Dispatchers.IO).async {
        return@async true
    }.await()
*/
}