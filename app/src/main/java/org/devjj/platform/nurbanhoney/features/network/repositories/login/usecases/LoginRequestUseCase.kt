package org.devjj.platform.nurbanhoney.features.network.repositories.login.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.login.LoginManager
import org.devjj.platform.nurbanhoney.features.ui.login.NurbanToken
import javax.inject.Inject

class LoginRequestUseCase
@Inject constructor(
    private val loginManager: LoginManager
    ) : UseCase<NurbanToken, LoginRequestUseCase.Params>(){
    override suspend fun run(params: Params) = loginManager.getNurbanToken(params.type, params.kakaoKey)
    data class Params(val type : String , val kakaoKey : String)
}