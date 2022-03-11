package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.RatingResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LikeUseCaseTest : UnitTest() {
    private lateinit var like: LikeUseCase

   var repository = mockk<ArticleRepository>()

    @BeforeEach
    fun setUp() {
        like = LikeUseCase(repository)
        every { repository.postLike(board, token, id) } returns Either.Right(RatingResponse.empty)
    }

    @Test
    fun `post like through repository`() {
        runBlocking { like.run(LikeUseCase.Params(board, token, id)) }
        verify(exactly = 1) { repository.postLike(board, token, id) }
    }

    companion object {
        private const val board = "nurban"
        private const val token = "token"
        private const val id = 1
    }
}