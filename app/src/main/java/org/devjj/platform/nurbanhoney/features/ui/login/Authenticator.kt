package org.devjj.platform.nurbanhoney.features.ui.login

import kotlinx.coroutines.*
import org.devjj.platform.nurbanhoney.features.network.repositories.login.LoginService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Authenticator
@Inject constructor(
    private val loginService: LoginService
) {
    suspend fun userLoggedIn(nurbanToken : String): Boolean = CoroutineScope(Dispatchers.IO).async {
        return@async loginService.validationCheck(
            nurbanToken
        ).execute().body()?.isValid ?: false
    }.await()
/*
    suspend fun userLoggedIn() :Boolean = CoroutineScope(Dispatchers.IO).async {
        return@async true
    }.await()
*/
}