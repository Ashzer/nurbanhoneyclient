package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.core.sharedpreference.Prefs
import org.devjj.platform.nurbanhoney.features.network.repositories.login.usecases.IsTokenValidUseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.login.usecases.LoginRequestUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val isTokenValid: IsTokenValidUseCase,
    private val loginRequest: LoginRequestUseCase
) : BaseViewModel() {
    val nurbanToken: MutableLiveData<NurbanToken> = MutableLiveData()
    val isValid: MutableLiveData<TokenStatus> = MutableLiveData()

    fun getNurbanToken(type: String, key: String) =
        loginRequest(LoginRequestUseCase.Params(type, key), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleToken
            )
        }

    fun isTokenValid(token: String) =
        isTokenValid(IsTokenValidUseCase.Params(token), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleToken
            )
        }


    private fun handleToken(token: NurbanToken) {
        nurbanToken.value = token
        Log.d("UseCase_login_check__", nurbanToken.value?.token ?: "")
        Log.d("UseCase_login_check__", nurbanToken.value?.error ?: "")
        if (!nurbanToken.equals(null)) {
            Prefs.token = nurbanToken.value?.token.toString()
            Prefs.userId = nurbanToken.value?.userId ?: -1
        }
    }

    private fun handleToken(token: TokenStatus) {
        isValid.value = token
    }

}