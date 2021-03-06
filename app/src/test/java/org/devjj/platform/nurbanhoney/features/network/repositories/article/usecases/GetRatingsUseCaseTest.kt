package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.Ratings
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRatingsUseCaseTest : UnitTest() {
    private lateinit var getRatings: GetRatingsUseCase

   var repository = mockk<ArticleRepository>()

    @BeforeEach
    fun setUp() {
        getRatings = GetRatingsUseCase(repository)

        every { repository.getRatings(board, token, articleId) } returns Either.Right(Ratings.empty)
    }

    @Test
    fun `get ratings from repository`() {
        runBlocking { getRatings.run(GetRatingsUseCase.Params(board, token, articleId)) }
        verify(exactly = 1) { repository.getRatings(board, token, articleId) }
    }

    companion object {
        private const val board = "nurban"
        private const val token = "token"
        private const val articleId = 1
    }
}