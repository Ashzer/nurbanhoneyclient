package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.CommentResponse
import org.junit.Before
import org.junit.Test

class PostCommentUseCaseTest : UnitTest() {
    private lateinit var postComment: PostCommentUseCase
    @MockK
    private lateinit var repository: ArticleRepository

    @Before
    fun setUp() {
        postComment = PostCommentUseCase(repository)
        every { repository.postComment(board, token, comment, id) } returns Either.Right(
            CommentResponse.empty)
    }

    @Test
    fun `post comment through repository`() {
        runBlocking { postComment.run(PostCommentUseCase.Params(board, token, comment, id)) }

        verify(exactly = 1) { repository.postComment(board, token, comment, id) }
    }

    companion object {
        private const val board = "nurban"
        private const val token = "token"
        private const val comment = "comment"
        private const val id = 1
    }
}