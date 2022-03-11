package org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.devjj.platform.nurbanhoney.features.ui.home.profile.Profile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GetProfileUseCaseTest : UnitTest() {
    private lateinit var getProfile: GetProfileUseCase
    var repository = mockk<ProfileRepository>()

    @BeforeEach
    fun setUp() {
        getProfile = GetProfileUseCase(repository)
        every { repository.getProfile(token) } returns Either.Right(Profile.empty)
    }

    @Test
    @DisplayName("프로필 조회")
    fun `get profile by token`() {
        runBlocking { getProfile.run(GetProfileUseCase.Params(token)) }

        verify(exactly = 1) { repository.getProfile(token) }
    }

    companion object {
        private const val token = "token"
    }
}