package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import io.mockk.every

import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.core.functional.Either.Right
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.CommentResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class DeleteCommentUseCaseTest : UnitTest() {

    private lateinit var deleteComment: DeleteCommentUseCase

    var repository = mockk<ArticleRepository>()

    @BeforeEach
    fun setUp() {
        deleteComment = DeleteCommentUseCase(repository)
        every { repository.deleteComment(board,token,id, articleId) } returns Right(CommentResponse.empty)
    }

    @Test
    fun `delete comment by id`(){
        runBlocking { deleteComment.run(DeleteCommentUseCase.Params(board,token,id, articleId)) }
        verify (exactly = 1){ repository.deleteComment(board, token,id, articleId) }
    }

    companion object{
        private const val board = "nurban"
        private const val token = "token"
        private const val id = 1
        private const val articleId = 1
    }
}