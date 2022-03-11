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

class GetProfileArticlesUseCaseTest : UnitTest() {
    private lateinit var getProfileArticles: GetProfileArticlesUseCase

    var repository = mockk<ProfileRepository>()

    @BeforeEach
    fun setUp() {
        getProfileArticles = GetProfileArticlesUseCase(repository)
        every { repository.getMyArticles(token, offset, limit) } returns Either.Right(listOf())
    }

    @Test
    @DisplayName("작성 글 조회")
    fun `get article list by user from repository`() {
        runBlocking {
            getProfileArticles.run(
                GetProfileArticlesUseCase.Params(
                    token,
                    offset,
                    limit
                )
            )
        }

        verify(exactly = 1) { repository.getMyArticles(token, offset, limit) }
    }

    companion object {
        private const val token = "token"
        private const val offset = 0
        private const val limit = 10
    }
}