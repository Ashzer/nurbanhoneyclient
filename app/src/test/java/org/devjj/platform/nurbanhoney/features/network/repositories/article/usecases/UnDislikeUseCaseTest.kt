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

class UnDislikeUseCaseTest : UnitTest() {
    private lateinit var unDislike: UnDislikeUseCase

    @MockK
    private lateinit var repository: ArticleRepository

    @Before
    fun setUp() {
        unDislike = UnDislikeUseCase(repository)
        every { repository.cancelDislike(board, token, id) } returns Either.Right(RatingResponse.empty)
    }

    @Test
    fun `cancel dislike through repository`() {
        runBlocking { unDislike.run(UnDislikeUseCase.Params(board, token, id)) }
        verify(exactly = 1) { repository.cancelDislike(board, token, id) }
    }

    companion object {
        private const val board = "nurban"
        private const val token = "token"
        private const val id = 1
    }
}