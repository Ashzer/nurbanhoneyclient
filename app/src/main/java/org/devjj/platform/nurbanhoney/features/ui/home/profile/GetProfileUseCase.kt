package org.devjj.platform.nurbanhoney.features.ui.home.profile

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class GetProfileUseCase
@Inject constructor(
    private val repository: ProfileRepository
) : UseCase<Profile, GetProfileUseCase.Params>(){
    override suspend fun run(params: Params) = repository.getProfile(params.token)

    data class Params(val token : String)
}