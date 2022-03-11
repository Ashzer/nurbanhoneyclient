package org.devjj.platform.nurbanhoney

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.core.functional.Either.Right
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases.GetProfileUseCase
import org.devjj.platform.nurbanhoney.features.ui.home.profile.Profile
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetProfileTest : UnitTest() {
    private lateinit var getProfile: GetProfileUseCase

    var profileRepository = mockk<ProfileRepository>()

    @BeforeEach
    fun setUp() {
        getProfile = GetProfileUseCase(profileRepository)
        every { profileRepository.getProfile(token) } returns Right(Profile.empty)
    }

    @Test
    fun `should get profile from repository`() {
        runBlocking {
            getProfile.run(GetProfileUseCase.Params(token))
        }

        verify(exactly = 1){ profileRepository.getProfile(token) }
    }

    companion object{
        var token = "1"
    }
}