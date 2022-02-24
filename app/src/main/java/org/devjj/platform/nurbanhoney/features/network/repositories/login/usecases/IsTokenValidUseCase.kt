package org.devjj.platform.nurbanhoney.features.network.repositories.login.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.login.LoginManager
import org.devjj.platform.nurbanhoney.features.ui.login.TokenStatus
import javax.inject.Inject

class IsTokenValidUseCase
@Inject constructor(
    private val loginManager: LoginManager
) : UseCase<TokenStatus, IsTokenValidUseCase.Params>() {
    override suspend fun run(params: Params) = loginManager.isTokenValid(params.token)
    data class Params(val token : String)
}