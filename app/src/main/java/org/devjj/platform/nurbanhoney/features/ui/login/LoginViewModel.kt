package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val isTokenValid: IsTokenValidUseCase,
    private val loginRequest : LoginRequestUseCase,
    private val prefs : SharedPreferences
) : BaseViewModel() {
    val nurbanToken : MutableLiveData<NurbanToken> = MutableLiveData()
    val isValid : MutableLiveData<TokenStatus> = MutableLiveData()

    fun getNurbanToken(type : String, key : String) =
        loginRequest(LoginRequestUseCase.Params(type, key), viewModelScope){
            it.fold(
                ::handleFailure,
                ::handleToken
            )
        }

    fun isTokenValid(token : String) =
        isTokenValid(IsTokenValidUseCase.Params(token), viewModelScope){
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
            editor.putString(prefsNurbanTokenKey, nurbanToken.value?.token.toString())
            editor.putString(prefsUserIdKey, nurbanToken.value?.userId.toString())
            editor.apply()
        }
    }

    private fun handleToken(token : TokenStatus){
        isValid.value = token
    }

}