package org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.devjj.platform.nurbanhoney.features.ui.home.profile.EditProfileResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EditProfileUseCaseTest : UnitTest() {
    private lateinit var editProfile: EditProfileUseCase

    var repository = mockk<ProfileRepository>()

    @BeforeEach
    fun setUp() {
        editProfile = EditProfileUseCase(repository)
        every {
            repository.editProfile(
                token,
                nickname,
                description,
                insignia
            )
        } returns Either.Right(
            EditProfileResponse("profile edited")
        )
    }

    @Test
    @DisplayName("프로필 수정")
    fun `edit profile through repository`() {
        runBlocking {
            editProfile.run(
                EditProfileUseCase.Params(
                    token,
                    nickname,
                    description,
                    insignia
                )
            )
        }

        verify(exactly = 1) { repository.editProfile(token, nickname, description, insignia) }
    }

    companion object {
        private const val token = "token"
        private const val nickname = "nickname"
        private const val description = "description"
        private val insignia = listOf<String>()
    }
}