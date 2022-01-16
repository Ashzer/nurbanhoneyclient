package org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.devjj.platform.nurbanhoney.features.ui.home.profile.EditProfileResponse
import javax.inject.Inject

class EditProfileUseCase
@Inject constructor(
    private val repository: ProfileRepository
) : UseCase<EditProfileResponse, EditProfileUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.editProfile(params.token, params.nickname, params.description, params.insignia)

    data class Params(
        val token: String,
        val nickname: String,
        val description: String,
        val insignia: List<String>
    )
}