package org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.devjj.platform.nurbanhoney.features.ui.home.profile.SignOutResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SignOutUseCaseTest : UnitTest() {
    private lateinit var signOut: SignOutUseCase
    var repository = mockk<ProfileRepository>()

    @BeforeEach
    fun setUp() {
        signOut = SignOutUseCase(repository)
        every { repository.signOut(token, id) } returns Either.Right(SignOutResponse.empty)
    }

    @Test
    @DisplayName("유저 로그아웃")
    fun `sign out through repository`() {
        runBlocking { signOut.run(SignOutUseCase.Params(token, id)) }

        verify(exactly = 1) { repository.signOut(token, id) }
    }

    companion object {
        private const val token = "token"
        private const val id = 0
    }
}