package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val isTokenValidUseCase: IsTokenValidUseCase,
    private val loginRequestUseCase : LoginRequestUseCase,
    private val prefs : SharedPreferences
) : BaseViewModel() {
    val nurbanToken : MutableLiveData<NurbanToken> = MutableLiveData()
    val isValid : MutableLiveData<TokenStatus> = MutableLiveData()

    fun getNurbanToken(type : String, kakaoKey : String) =
        loginRequestUseCase(LoginRequestUseCase.Params(type, kakaoKey), viewModelScope){
            it.fold(
                ::handleFailure,
                ::handleToken
            )
        }

    fun isTokenValid(token : String) =
        isTokenValidUseCase(IsTokenValidUseCase.Params(token), viewModelScope){
            it.fold(
                ::handleFailure,
                ::handleToken
            )
        }


    private fun handleToken(token : NurbanToken){
        nurbanToken.value = token
        Log.d("UseCase_login_check__" , nurbanToken.value?.token ?: "")
        Log.d("UseCase_login_check__" , nurbanToken.value?.error ?: "")
        if(!nurbanToken.equals(null)){
            var editor = prefs.edit()
            editor.putString("NurbanToken", nurbanToken.value?.token.toString())
            editor.apply()
        }
    }

    private fun handleToken(token : TokenStatus){
        isValid.value = token
    }

    fun getToken(): String{
        return nurbanToken.value?.token ?: ""
    }
}