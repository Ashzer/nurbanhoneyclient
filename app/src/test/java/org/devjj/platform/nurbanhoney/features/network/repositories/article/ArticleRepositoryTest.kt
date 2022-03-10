package org.devjj.platform.nurbanhoney.features.network.repositories.article

import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.exception.Failure.NetworkConnection
import org.devjj.platform.nurbanhoney.core.exception.Failure.ServerError
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.network.entities.ArticlesRequestEntity
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class ArticleRepositoryTest : UnitTest() {

    private lateinit var networkRepository: ArticleRepository.Network

    @MockK
    private lateinit var networkHandler: NetworkHandler

    @MockK
    private lateinit var service: ArticleService

    @MockK
    private lateinit var getArticlesCall: Call<List<ArticlesRequestEntity>>

    @MockK
    private lateinit var getArticlesResponse: Response<List<ArticlesRequestEntity>>

    @Before
    fun setUp() {
        networkRepository = ArticleRepository.Network(networkHandler, service)
    }

    @Test
    fun `should return empty list by default`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { getArticlesResponse.body() } returns null
        every { getArticlesResponse.isSuccessful } returns true
        every { getArticlesCall.execute() } returns getArticlesResponse
        every { service.getArticles(board, flag, offset, limit) } returns getArticlesCall

        val articles = networkRepository.getArticles(board, flag, offset, limit)

        articles shouldEqual Either.Right(emptyList<Article>())
        verify(exactly = 1) { service.getArticles(board, flag, offset, limit) }
    }

    @Test
    fun `should get article list from service`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { getArticlesResponse.body() } returns listOf(
            ArticlesRequestEntity(
                1, String.empty(), String.empty(), 0, 0,
                Board.empty,
                ArticlesRequestEntity.User(1, String.empty(), String.empty())
            )
        )
        every { getArticlesResponse.isSuccessful } returns true
        every { getArticlesCall.execute() } returns getArticlesResponse
        every { service.getArticles(board, flag, offset, limit) } returns getArticlesCall

        val articles = networkRepository.getArticles(board, flag, offset, limit)

        articles shouldEqual Either.Right(
            listOf(
                ArticleItem(
                    1,
                    String.empty(),
                    String.empty(),
                    0,
                    Board.empty,
                    String.empty(),
                    String.empty()
                )
            )
        )
        verify(exactly = 1) { service.getArticles(board, flag, offset, limit) }
    }

    @Test
    fun `article service should return network failure when no connection`() {
        every { networkHandler.isNetworkAvailable() } returns false

        val articles = networkRepository.getArticles(board, flag, offset, limit)
        articles shouldBeInstanceOf Either::class.java
        articles.isLeft shouldEqual true
        articles.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
            {})
        verify { service wasNot Called }
    }

    @Test
    fun `article service should return sever error if no successful response`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { getArticlesResponse.isSuccessful } returns false
        every { getArticlesCall.execute() } returns getArticlesResponse
        every { service.getArticles(board, flag, offset, limit) } returns getArticlesCall

        val articles = networkRepository.getArticles(board, flag, offset, limit)

        articles shouldBeInstanceOf Either::class.java
        articles.isLeft shouldEqual true
        articles.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `article request should catch exceptions`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { getArticlesCall.execute() } returns getArticlesResponse
        every { service.getArticles(board, flag, offset, limit) } returns getArticlesCall

        val articles = networkRepository.getArticles(board, flag, offset, limit)

        articles shouldBeInstanceOf Either::class.java
        articles.isLeft shouldEqual true
        articles.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }



    companion object {
        private const val board = "nurban"
        private const val flag = 0
        private const val offset = 0
        private const val limit = 10
        private const val token = "token"
        private const val articleId = 1
        private const val id = 1
    }

    fun testGetArticle() {}

    fun testPostLike() {}

    fun testCancelLike() {}

    fun testPostDislike() {}

    fun testCancelDislike() {}

    fun testGetRatings() {}

    fun testPostComment() {}

    fun testGetComments() {}

    fun testDeleteComment() {}

    fun testUpdateComment() {}

    fun testGetComment() {}

    fun testGetArticles() {}
}