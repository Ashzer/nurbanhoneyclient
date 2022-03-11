package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.Comment
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCommentUseCaseTest : UnitTest() {
    private lateinit var getComment: GetCommentUseCase

   var repository = mockk<ArticleRepository>()

    @BeforeEach
    fun setUp() {
        getComment = GetCommentUseCase(repository)
        every { repository.getComment(board, commentId) } returns Either.Right(Comment.empty)
    }

    @Test
    fun `get a comment from repository`() {
        runBlocking { getComment.run(GetCommentUseCase.Params(board, commentId)) }

        verify(exactly = 1) { repository.getComment(board, commentId) }
    }

    companion object {
        private const val board = "nurban"
        private const val commentId = 1
    }
}