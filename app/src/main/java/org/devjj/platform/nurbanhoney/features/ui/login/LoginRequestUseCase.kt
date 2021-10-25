package org.devjj.platform.nurbanhoney.features.ui.login

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class LoginRequestUseCase
@Inject constructor(
    private val loginManager: LoginManager
    ) : UseCase<NurbanToken, LoginRequestUseCase.Params>(){
    override suspend fun run(params: Params) = loginManager.getNurbanToken(params.type, params.kakaoKey)
    data class Params(val type : String , val kakaoKey : String)
}