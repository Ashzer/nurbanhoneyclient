package org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.ProfileRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GetProfileCommentsUseCaseTest : UnitTest() {
    private lateinit var getProfileComments: GetProfileCommentsUseCase
    var repository = mockk<ProfileRepository>()

    @BeforeEach
    fun setUp() {
        getProfileComments = GetProfileCommentsUseCase(repository)
        every { repository.getMyComments(token, offset, limit) } returns Either.Right(listOf())
    }

    @Test
    @DisplayName("작성 댓글 조회")
    fun `get comment list by user from repository`() {
        runBlocking {
            getProfileComments.run(
                GetProfileCommentsUseCase.Params(
                    token,
                    offset,
                    limit
                )
            )
        }

        verify(exactly = 1) { repository.getMyComments(token, offset, limit) }
    }

    companion object {
        private const val token = "token"
        private const val offset = 0
        private const val limit = 10
    }
}