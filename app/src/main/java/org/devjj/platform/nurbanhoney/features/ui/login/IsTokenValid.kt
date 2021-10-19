package org.devjj.platform.nurbanhoney.features.ui.login

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class IsTokenValid
@Inject constructor(
    private val loginManager: LoginManager
) : UseCase<TokenValidation , IsTokenValid.Params>() {
    override suspend fun run(params: Params) = loginManager.isTokenValid(params.token)
    data class Params(val token : String)
}