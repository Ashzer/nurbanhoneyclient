package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetArticleUseCaseTest : UnitTest() {
    private lateinit var getArticle: GetArticleUseCase

    var repository = mockk<ArticleRepository>()

    @BeforeEach
    fun setUp() {
        getArticle = GetArticleUseCase(repository)
        every { repository.getArticle(board, token, id) } returns Either.Right(Article.empty)
    }

    @Test
    fun `get an article from repository`() {
        runBlocking { getArticle.run(GetArticleUseCase.Params(board, token, id)) }

        verify(exactly = 1) { repository.getArticle(board, token, id) }
    }

    companion object {
        private const val board = "nurban"
        private const val token = "token"
        private const val id = 1

    }
}