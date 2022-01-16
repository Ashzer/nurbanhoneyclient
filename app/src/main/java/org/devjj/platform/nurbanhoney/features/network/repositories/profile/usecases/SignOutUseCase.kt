package org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.devjj.platform.nurbanhoney.features.ui.home.profile.SignOutResponse
import javax.inject.Inject

class SignOutUseCase
@Inject constructor(
    private val repository: ProfileRepository
) : UseCase<SignOutResponse, SignOutUseCase.Params>() {
    override suspend fun run(params: Params) = repository.signOut(params.token, params.id)

    data class Params(val token: String, val id: Int)
}