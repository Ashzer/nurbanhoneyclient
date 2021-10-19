package org.devjj.platform.nurbanhoney.features.ui.login

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.functional.Either.Left
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.LoginService
import org.devjj.platform.nurbanhoney.features.network.ValidationEntity
import javax.inject.Inject

interface LoginManager {
    fun getNurbanToken(type : String ,kakaoKey : String) : Either<Failure, NurbanToken>
    fun isTokenValid(token : String) : Either<Failure,TokenValidation>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val loginService: LoginService
    ) : LoginManager {

        override fun getNurbanToken(type: String, key : String): Either<Failure, NurbanToken> {
            return when( networkHandler.isNetworkAvailable()){
                true ->
                    networkHandler.request(
                        loginService.loginRequest(type, key),
                        { it.toNurbanToken() },
                        LoginEntity.empty
                    )
                false -> Left(Failure.NetworkConnection)
            }
        }

        override fun isTokenValid(token: String): Either<Failure, TokenValidation> {
            return when( networkHandler.isNetworkAvailable()){
                true ->
                    networkHandler.request(
                        loginService.validationCheck(token),
                        { it.toIsTokenValid() },
                        ValidationEntity.empty
                    )
                false -> Left(Failure.NetworkConnection)
            }
        }
    }
}