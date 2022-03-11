package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCommentsUseCaseTest : UnitTest() {
    private lateinit var getComments: GetCommentsUseCase
   var repository = mockk<ArticleRepository>()

    @BeforeEach
    fun setUp() {
        getComments = GetCommentsUseCase(repository)
        every { repository.getComments(board, articleId, offset, limit) } returns Either.Right(
            listOf()
        )
    }

    @Test
    fun `get comments from repository`() {
        runBlocking { getComments.run(GetCommentsUseCase.Params(board, articleId, offset, limit)) }

        verify(exactly = 1) { repository.getComments(board, articleId, offset, limit) }
    }

    companion object {
        private const val board = "nurban"
        private const val articleId = 1
        private const val offset = 0
        private const val limit = 10
    }
}