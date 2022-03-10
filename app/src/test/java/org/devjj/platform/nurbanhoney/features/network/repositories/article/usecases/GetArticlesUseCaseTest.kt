package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.junit.Before
import org.junit.Test

class GetArticlesUseCaseTest : UnitTest() {
    private lateinit var getArticles : GetArticlesUseCase

    @MockK private lateinit var repository: ArticleRepository

    @Before
    fun setUp(){
        getArticles = GetArticlesUseCase(repository)

        every { repository.getArticles(board, flag, offset, limit) } returns Either.Right(listOf())
    }

    @Test
    fun `get articles from repository`(){
        runBlocking { getArticles.run(GetArticlesUseCase.Params(board, flag, offset, limit)) }

        verify(exactly = 1) { repository.getArticles(board, flag, offset, limit) }
    }

    companion object {
        private const val board = "nurban"
        private const val flag = 0
        private const val offset = 0
        private const val limit = 10
    }
}