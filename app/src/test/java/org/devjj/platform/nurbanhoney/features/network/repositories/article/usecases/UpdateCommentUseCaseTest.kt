package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.CommentResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateCommentUseCaseTest : UnitTest() {
    private lateinit var updateComment: UpdateCommentUseCase

   var repository = mockk<ArticleRepository>()

    @BeforeEach
    fun setUp() {
        updateComment = UpdateCommentUseCase(repository)
        every { repository.updateComment(board, token, id, content) } returns Either.Right(
            CommentResponse.empty
        )
    }

    @Test
    fun `update comment through repository`() {
        runBlocking { updateComment.run(UpdateCommentUseCase.Params(board, token, id, content)) }
        verify(exactly = 1) { repository.updateComment(board, token, id, content) }
    }

    companion object {
        private const val board = "nurban"
        private const val token = "token"
        private const val id = 1
        private const val content = "content"
    }
}