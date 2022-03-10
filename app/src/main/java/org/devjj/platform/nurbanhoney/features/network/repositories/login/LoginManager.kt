package org.devjj.platform.nurbanhoney.features.network.repositories.login

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.functional.Either.Left
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.entities.LoginEntity
import org.devjj.platform.nurbanhoney.features.network.entities.ValidationEntity
import org.devjj.platform.nurbanhoney.features.network.request
import org.devjj.platform.nurbanhoney.features.ui.login.NurbanToken
import org.devjj.platform.nurbanhoney.features.ui.login.TokenStatus
import javax.inject.Inject

interface LoginManager {
    fun getNurbanToken(type: String, kakaoKey: String): Either<Failure, NurbanToken>
    fun isTokenValid(token: String): Either<Failure, TokenStatus>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val loginService: LoginService
    ) : LoginManager {

        override fun getNurbanToken(type: String, kakaoKey: String): Either<Failure, NurbanToken> {
            return when (networkHandler.isNetworkAvailable()) {
                true ->
                    request(
                        loginService.loginRequest(type, kakaoKey),
                        { it.toNurbanToken() },
                        LoginEntity.empty
                    )
                false -> Left(Failure.NetworkConnection)
            }
        }

        override fun isTokenValid(token: String): Either<Failure, TokenStatus> {
            return when (networkHandler.isNetworkAvailable()) {
                true ->
                    request(
                        loginService.validationCheck(token),
                        { it.toIsTokenValid() },
                        ValidationEntity.empty
                    )
                false -> Left(Failure.NetworkConnection)
            }
        }
    }
}