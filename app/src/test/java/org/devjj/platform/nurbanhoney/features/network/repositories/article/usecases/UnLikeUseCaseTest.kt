package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.RatingResponse
import org.junit.Before
import org.junit.Test

class UnLikeUseCaseTest : UnitTest() {
    private lateinit var unLike: UnLikeUseCase

    @MockK
    private lateinit var repository: ArticleRepository

    @Before
    fun setUp() {
        unLike = UnLikeUseCase(repository)
        every { repository.cancelLike(board, token, id) } returns Either.Right(RatingResponse.empty)
    }

    @Test
    fun `cancel like through repository`() {
        runBlocking { unLike.run(UnLikeUseCase.Params(board, token, id)) }
        verify(exactly = 1) { repository.cancelLike(board, token, id) }
    }

    companion object {
        private const val board = "nurban"
        private const val token = "token"
        private const val id = 1
    }
}