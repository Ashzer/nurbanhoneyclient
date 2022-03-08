package org.devjj.platform.nurbanhoney

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.core.functional.Either.Right
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases.GetProfileUseCase
import org.devjj.platform.nurbanhoney.features.ui.home.profile.Profile
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.junit.Before
import org.junit.Test

class GetProfileTest : UnitTest() {
    private lateinit var getProfile: GetProfileUseCase

    @MockK
    private lateinit var profileRepository: ProfileRepository

    @Before
    fun setUp() {
        getProfile = GetProfileUseCase(profileRepository)
        every { profileRepository.getProfile(token) } returns Right(Profile.empty)
    }

    @Test
    fun `should get profile from repository`() {
        runBlocking {
            profileRepository.run { (GetProfileUseCase.Params(token)) }
        }

        verify(exactly = 1){ profileRepository.getProfile(token) }
    }

    companion object{
        var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkiOiJLLTE5MzY3NzY2NTEiLCJpYXQiOjE2NDIwMjczMzMsImV4cCI6MTY0MjAyNzYzM30.qA8v1-kfzpe9Us9svhy_KqrAgfYywidM96k-cd0OZbM"
    }
}