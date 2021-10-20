package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.SharedPreferences
import android.media.session.MediaSession
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val isTokenValid: IsTokenValid,
    private val loginRequest : LoginRequest,
    private val prefs : SharedPreferences
) : BaseViewModel() {
    val nurbanToken : MutableLiveData<NurbanToken> = MutableLiveData()
    private var hasToken : Boolean = false
    val isValid : MutableLiveData<TokenValidation> = MutableLiveData()

    fun getNurbanToken(type : String, kakaoKey : String) =
        loginRequest(LoginRequest.Params(type, kakaoKey), viewModelScope){
            it.fold(
                ::handleFailure,
                ::handleToken
            )
        }

    fun isTokenValid(token : String) =
        isTokenValid(IsTokenValid.Params(token), viewModelScope){
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
            hasToken = true
        }
    }

    private fun handleToken(token : TokenValidation){
        isValid.value = token
    }

    fun getToken(): String{
        return nurbanToken.value?.token ?: ""
    }
}