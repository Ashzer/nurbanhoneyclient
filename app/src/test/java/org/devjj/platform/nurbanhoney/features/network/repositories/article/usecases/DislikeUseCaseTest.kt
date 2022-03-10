package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either.Right
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.RatingResponse
import org.junit.Before
import org.junit.Test

class DislikeUseCaseTest : UnitTest() {

    private lateinit var dislike: DislikeUseCase

    @MockK
    private lateinit var repository: ArticleRepository

    @Before
    fun setUp() {
        dislike = DislikeUseCase(repository)
        every { repository.postDislike(board, token, id) } returns Right(RatingResponse.empty)
    }

    @Test
    fun `post dislike through repository`() {
        runBlocking { dislike.run(DislikeUseCase.Params(board, token, id)) }

        verify { repository.postDislike(board, token, id) }
    }

    companion object {
        private const val board = "nurban"
        private const val token = "token"
        private const val id = 1
    }
}