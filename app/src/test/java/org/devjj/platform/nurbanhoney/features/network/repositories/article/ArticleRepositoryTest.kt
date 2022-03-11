package org.devjj.platform.nurbanhoney.features.network.repositories.article

import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.exception.Failure.NetworkConnection
import org.devjj.platform.nurbanhoney.core.exception.Failure.ServerError
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.functional.Either.Right
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.network.entities.*
import org.devjj.platform.nurbanhoney.features.ui.article.model.*
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Response

@DisplayName("Article Repository 테스트")
class ArticleRepositoryTest : UnitTest() {

    companion object {

        const val board = "nurban"
        const val flag = 0
        const val offset = 0
        const val limit = 10
        const val token = "token"
        const val articleId = 1
        const val id = 1
        const val comment = "comment"
        const val commentId = 1
    }

    lateinit var networkRepository: ArticleRepository.Network
    var networkHandler = mockk<NetworkHandler>()
    var service = mockk<ArticleService>()

    @BeforeEach
    fun setUp() {
        networkRepository = ArticleRepository.Network(networkHandler, service)
    }

    @Nested
    @DisplayName("Get Articles 테스트")
    inner class GetArticlesTest {

        private val getArticlesCall = mockk<Call<List<ArticlesRequestEntity>>>()
        private val getArticlesResponse = mockk<Response<List<ArticlesRequestEntity>>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty list")
        fun `should return empty list by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getArticlesResponse.body() } returns null
            every { getArticlesResponse.isSuccessful } returns true
            every { getArticlesCall.execute() } returns getArticlesResponse
            every { service.getArticles(board, flag, offset, limit) } returns getArticlesCall

            val articles = networkRepository.getArticles(board, flag, offset, limit)

            articles shouldEqual Right(emptyList<Article>())
            verify(exactly = 1) { service.getArticles(board, flag, offset, limit) }
        }

        @Test
        @DisplayName("정상 작동 : article 리스트 가져옴")
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

            articles shouldEqual Right(
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
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
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
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
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
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getArticlesCall.execute() } returns getArticlesResponse
            every { service.getArticles(board, flag, offset, limit) } returns getArticlesCall

            val articles = networkRepository.getArticles(board, flag, offset, limit)

            articles shouldBeInstanceOf Either::class.java
            articles.isLeft shouldEqual true
            articles.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Get Article 테스트")
    inner class GetArticleTest {
        private val getArticleCall = mockk<Call<ArticleEntity>>()
        private val getArticleResponse = mockk<Response<ArticleEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty article by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getArticleResponse.body() } returns null
            every { getArticleResponse.isSuccessful } returns true
            every { getArticleCall.execute() } returns getArticleResponse
            every { service.getArticle(board, token, id) } returns getArticleCall

            val article = networkRepository.getArticle(board, token, id)

            article shouldEqual Right(Article.empty)
            verify(exactly = 1) { service.getArticle(board, token, id) }
        }

        @Test
        @DisplayName("정상 작동 : article 가져옴")
        fun `should get an article from service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getArticleResponse.body() } returns ArticleEntity.empty
            every { getArticleResponse.isSuccessful } returns true
            every { getArticleCall.execute() } returns getArticleResponse
            every { service.getArticle(board, token, id) } returns getArticleCall

            val article = networkRepository.getArticle(board, token, id)

            article shouldEqual Right(Article.empty)
            verify(exactly = 1) { service.getArticle(board, token, id) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val article = networkRepository.getArticle(board, token, id)
            article shouldBeInstanceOf Either::class.java
            article.isLeft shouldEqual true
            article.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getArticleResponse.isSuccessful } returns false
            every { getArticleCall.execute() } returns getArticleResponse
            every { service.getArticle(board, token, id) } returns getArticleCall

            val article = networkRepository.getArticle(board, token, id)

            article shouldBeInstanceOf Either::class.java
            article.isLeft shouldEqual true
            article.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getArticleCall.execute() } returns getArticleResponse
            every { service.getArticle(board, token, id) } returns getArticleCall

            val article = networkRepository.getArticle(board, token, id)

            article shouldBeInstanceOf Either::class.java
            article.isLeft shouldEqual true
            article.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Post Like 테스트")
    inner class PostLikeTest {
        private val postLikeCall = mockk<Call<SimpleResponseEntity>>()
        private val postLikeResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty rating response by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postLikeResponse.body() } returns null
            every { postLikeResponse.isSuccessful } returns true
            every { postLikeCall.execute() } returns postLikeResponse
            every { service.postLike(board, token, id) } returns postLikeCall

            val response = networkRepository.postLike(board, token, id)
            response shouldEqual Right(RatingResponse.empty)
            verify(exactly = 1) { service.postLike(board, token, id) }
        }

        @Test
        @DisplayName("정상 작동 : like posted")
        fun `like has posted`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postLikeResponse.body() } returns SimpleResponseEntity("liked")
            every { postLikeResponse.isSuccessful } returns true
            every { postLikeCall.execute() } returns postLikeResponse
            every { service.postLike(board, token, id) } returns postLikeCall

            val response = networkRepository.postLike(board, token, id)

            response shouldEqual Right(RatingResponse("liked"))
            verify(exactly = 1) { service.postLike(board, token, id) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.postLike(board, token, id)
            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postLikeResponse.isSuccessful } returns false
            every { postLikeCall.execute() } returns postLikeResponse
            every { service.postLike(board, token, id) } returns postLikeCall

            val response = networkRepository.postLike(board, token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postLikeCall.execute() } returns postLikeResponse
            every { service.postLike(board, token, id) } returns postLikeCall

            val response = networkRepository.postLike(board, token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Cancel Like 테스트")
    inner class CancelLikeTest {
        private val cancelLikeCall = mockk<Call<SimpleResponseEntity>>()
        private val cancelLikeResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty rating response by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { cancelLikeResponse.body() } returns null
            every { cancelLikeResponse.isSuccessful } returns true
            every { cancelLikeCall.execute() } returns cancelLikeResponse
            every { service.cancelLike(board, token, id) } returns cancelLikeCall

            val response = networkRepository.cancelLike(board, token, id)
            response shouldEqual Right(RatingResponse.empty)
            verify(exactly = 1) { service.cancelLike(board, token, id) }
        }

        @Test
        @DisplayName("정상 작동 : cancel liked posted")
        fun `cancel liked has posted`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { cancelLikeResponse.body() } returns SimpleResponseEntity("like has canceled")
            every { cancelLikeResponse.isSuccessful } returns true
            every { cancelLikeCall.execute() } returns cancelLikeResponse
            every { service.cancelLike(board, token, id) } returns cancelLikeCall

            val response = networkRepository.cancelLike(board, token, id)

            response shouldEqual Right(RatingResponse("like has canceled"))
            verify(exactly = 1) { service.cancelLike(board, token, id) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.cancelLike(board, token, id)
            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { cancelLikeResponse.isSuccessful } returns false
            every { cancelLikeCall.execute() } returns cancelLikeResponse
            every { service.cancelLike(board, token, id) } returns cancelLikeCall

            val response = networkRepository.cancelLike(board, token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { cancelLikeCall.execute() } returns cancelLikeResponse
            every { service.cancelLike(board, token, id) } returns cancelLikeCall

            val response = networkRepository.cancelLike(board, token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Post Dislike 테스트")
    inner class PostDislikeTest {
        private val postDislikeCall = mockk<Call<SimpleResponseEntity>>()
        private val postDislikeResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty rating response by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postDislikeResponse.body() } returns null
            every { postDislikeResponse.isSuccessful } returns true
            every { postDislikeCall.execute() } returns postDislikeResponse
            every { service.postDislike(board, token, id) } returns postDislikeCall

            val response = networkRepository.postDislike(board, token, id)
            response shouldEqual Right(RatingResponse.empty)
            verify(exactly = 1) { service.postDislike(board, token, id) }
        }

        @Test
        @DisplayName("정상 작동 : dislike posted")
        fun `dislike has posted`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postDislikeResponse.body() } returns SimpleResponseEntity("disliked")
            every { postDislikeResponse.isSuccessful } returns true
            every { postDislikeCall.execute() } returns postDislikeResponse
            every { service.postDislike(board, token, id) } returns postDislikeCall

            val response = networkRepository.postDislike(board, token, id)

            response shouldEqual Right(RatingResponse("disliked"))
            verify(exactly = 1) { service.postDislike(board, token, id) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.postDislike(board, token, id)
            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postDislikeResponse.isSuccessful } returns false
            every { postDislikeCall.execute() } returns postDislikeResponse
            every { service.postDislike(board, token, id) } returns postDislikeCall

            val response = networkRepository.postDislike(board, token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postDislikeCall.execute() } returns postDislikeResponse
            every { service.postDislike(board, token, id) } returns postDislikeCall

            val response = networkRepository.postDislike(board, token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Cancel Dislike 테스트")
    inner class CancelDislikeTest {
        private val cancelDislikeCall = mockk<Call<SimpleResponseEntity>>()
        private val cancelDislikeResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty rating response by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { cancelDislikeResponse.body() } returns null
            every { cancelDislikeResponse.isSuccessful } returns true
            every { cancelDislikeCall.execute() } returns cancelDislikeResponse
            every { service.cancelDislike(board, token, id) } returns cancelDislikeCall

            val response = networkRepository.cancelDislike(board, token, id)
            response shouldEqual Right(RatingResponse.empty)
            verify(exactly = 1) { service.cancelDislike(board, token, id) }
        }

        @Test
        @DisplayName("정상 작동 : cancel disliked posted")
        fun `cancel disliked has posted`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { cancelDislikeResponse.body() } returns SimpleResponseEntity("dislike has canceled")
            every { cancelDislikeResponse.isSuccessful } returns true
            every { cancelDislikeCall.execute() } returns cancelDislikeResponse
            every { service.cancelDislike(board, token, id) } returns cancelDislikeCall

            val response = networkRepository.cancelDislike(board, token, id)

            response shouldEqual Right(RatingResponse("dislike has canceled"))
            verify(exactly = 1) { service.cancelDislike(board, token, id) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.cancelDislike(board, token, id)
            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { cancelDislikeResponse.isSuccessful } returns false
            every { cancelDislikeCall.execute() } returns cancelDislikeResponse
            every { service.cancelDislike(board, token, id) } returns cancelDislikeCall

            val response = networkRepository.cancelDislike(board, token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { cancelDislikeCall.execute() } returns cancelDislikeResponse
            every { service.cancelDislike(board, token, id) } returns cancelDislikeCall

            val response = networkRepository.cancelDislike(board, token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Get Ratings 테스트")
    inner class GetRatingsTest {
        private val getRatingsCall = mockk<Call<RatingsEntity>>()
        private val getRatingsResponse = mockk<Response<RatingsEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty rating response by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getRatingsResponse.body() } returns null
            every { getRatingsResponse.isSuccessful } returns true
            every { getRatingsCall.execute() } returns getRatingsResponse
            every { service.getRatings(board, token, articleId) } returns getRatingsCall

            val ratings = networkRepository.getRatings(board, token, articleId)
            ratings shouldEqual Right(Ratings.empty)
            verify(exactly = 1) { service.getRatings(board, token, articleId) }
        }

        @Test
        @DisplayName("정상 작동 : get ratings")
        fun `got ratings from service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getRatingsResponse.body() } returns RatingsEntity(1, 1, "like")
            every { getRatingsResponse.isSuccessful } returns true
            every { getRatingsCall.execute() } returns getRatingsResponse
            every { service.getRatings(board, token, articleId) } returns getRatingsCall

            val ratings = networkRepository.getRatings(board, token, articleId)

            ratings shouldEqual Right(Ratings(1, 1, "like"))
            verify(exactly = 1) { service.getRatings(board, token, articleId) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val ratings = networkRepository.getRatings(board, token, articleId)
            ratings shouldBeInstanceOf Either::class.java
            ratings.isLeft shouldEqual true
            ratings.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getRatingsResponse.isSuccessful } returns false
            every { getRatingsCall.execute() } returns getRatingsResponse
            every { service.getRatings(board, token, articleId) } returns getRatingsCall

            val response = networkRepository.getRatings(board, token, articleId)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getRatingsCall.execute() } returns getRatingsResponse
            every { service.getRatings(board, token, articleId) } returns getRatingsCall

            val ratings = networkRepository.getRatings(board, token, articleId)

            ratings shouldBeInstanceOf Either::class.java
            ratings.isLeft shouldEqual true
            ratings.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Post Comment")
    inner class PostCommentTest {
        private val postCommentCall = mockk<Call<SimpleResponseEntity>>()
        private val postCommentResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty response by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postCommentResponse.body() } returns null
            every { postCommentResponse.isSuccessful } returns true
            every { postCommentCall.execute() } returns postCommentResponse
            every { service.postComment(board, token, comment, id) } returns postCommentCall

            val response = networkRepository.postComment(board, token, comment, id)
            response shouldEqual Right(CommentResponse.empty)
            verify(exactly = 1) { service.postComment(board, token, comment, id) }
        }

        @Test
        @DisplayName("정상 작동 : comment posted")
        fun `a comment has posted`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postCommentResponse.body() } returns SimpleResponseEntity("comment posted")
            every { postCommentResponse.isSuccessful } returns true
            every { postCommentCall.execute() } returns postCommentResponse
            every { service.postComment(board, token, comment, id) } returns postCommentCall

            val response = networkRepository.postComment(board, token, comment, id)

            response shouldEqual Right(CommentResponse("comment posted"))
            verify(exactly = 1) { service.postComment(board, token, comment, id) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.postComment(board, token, comment, id)
            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postCommentResponse.isSuccessful } returns false
            every { postCommentCall.execute() } returns postCommentResponse
            every { service.postComment(board, token, comment, id) } returns postCommentCall

            val response = networkRepository.postComment(board, token, comment, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { postCommentCall.execute() } returns postCommentResponse
            every { service.postComment(board, token, comment, id) } returns postCommentCall

            val response = networkRepository.postComment(board, token, comment, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Get Comments 테스트")
    inner class GetCommentsTest {
        private val getCommentsCall = mockk<Call<List<CommentEntity>>>()
        private val getCommentsResponse = mockk<Response<List<CommentEntity>>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty list")
        fun `should return empty list by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getCommentsResponse.body() } returns null
            every { getCommentsResponse.isSuccessful } returns true
            every { getCommentsCall.execute() } returns getCommentsResponse
            every { service.getComments(board, id, offset, limit) } returns getCommentsCall

            val comments = networkRepository.getComments(board, articleId, offset, limit)

            comments shouldEqual Right(emptyList<Comment>())
            verify(exactly = 1) { service.getComments(board, id, offset, limit) }
        }

        @Test
        @DisplayName("정상 작동 : comment 리스트 가져옴")
        fun `should get comment list from service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getCommentsResponse.body() } returns listOf(
                CommentEntity(
                    1, "comment", 1,
                    CommentEntity.User(1, "badge", "nickname", null)
                )
            )
            every { getCommentsResponse.isSuccessful } returns true
            every { getCommentsCall.execute() } returns getCommentsResponse
            every { service.getComments(board, id, offset, limit) } returns getCommentsCall

            val comments = networkRepository.getComments(board, articleId, offset, limit)

            comments shouldEqual Right(
                listOf(
                    Comment(1, "comment", 1, 1, "badge", "nickname", listOf())
                )
            )

            verify(exactly = 1) { service.getComments(board, id, offset, limit) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val comments = networkRepository.getComments(board, articleId, offset, limit)
            comments shouldBeInstanceOf Either::class.java
            comments.isLeft shouldEqual true
            comments.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getCommentsResponse.isSuccessful } returns false
            every { getCommentsCall.execute() } returns getCommentsResponse
            every { service.getComments(board, id, offset, limit) } returns getCommentsCall

            val comments = networkRepository.getComments(board, articleId, offset, limit)

            comments shouldBeInstanceOf Either::class.java
            comments.isLeft shouldEqual true
            comments.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getCommentsCall.execute() } returns getCommentsResponse
            every { service.getComments(board, id, offset, limit) } returns getCommentsCall

            val comments = networkRepository.getComments(board, articleId, offset, limit)

            comments shouldBeInstanceOf Either::class.java
            comments.isLeft shouldEqual true
            comments.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

    }

    @Nested
    @DisplayName("Delete Comment 테스트")
    inner class DeleteCommentTest {
        private val deleteCommentCall = mockk<Call<SimpleResponseEntity>>()
        private val deleteCommentResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty response by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { deleteCommentResponse.body() } returns null
            every { deleteCommentResponse.isSuccessful } returns true
            every { deleteCommentCall.execute() } returns deleteCommentResponse
            every { service.deleteComment(board, token, id, articleId) } returns deleteCommentCall

            val response = networkRepository.deleteComment(board, token, id, articleId)

            response shouldEqual Right(CommentResponse.empty)
            verify(exactly = 1) { service.deleteComment(board, token, id, articleId) }
        }

        @Test
        @DisplayName("정상 작동 : comment 삭제")
        fun `should delete a comment through service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { deleteCommentResponse.body() } returns SimpleResponseEntity("comment deleted")
            every { deleteCommentResponse.isSuccessful } returns true
            every { deleteCommentCall.execute() } returns deleteCommentResponse
            every { service.deleteComment(board, token, id, articleId) } returns deleteCommentCall

            val response = networkRepository.deleteComment(board, token, id, articleId)

            response shouldEqual Right(CommentResponse("comment deleted"))
            verify(exactly = 1) { service.deleteComment(board, token, id, articleId) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.deleteComment(board, token, id, articleId)
            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { deleteCommentResponse.isSuccessful } returns false
            every { deleteCommentCall.execute() } returns deleteCommentResponse
            every { service.deleteComment(board, token, id, articleId) } returns deleteCommentCall

            val response = networkRepository.deleteComment(board, token, id, articleId)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { deleteCommentCall.execute() } returns deleteCommentResponse
            every { service.deleteComment(board, token, id, articleId) } returns deleteCommentCall

            val response = networkRepository.deleteComment(board, token, id, articleId)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Update Comment 테스트")
    inner class UpdateCommentTest {
        private val updateCommentCall = mockk<Call<SimpleResponseEntity>>()
        private val updateCommentResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty response by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { updateCommentResponse.body() } returns null
            every { updateCommentResponse.isSuccessful } returns true
            every { updateCommentCall.execute() } returns updateCommentResponse
            every { service.updateComment(board, token, id, comment) } returns updateCommentCall

            val response = networkRepository.updateComment(board, token, id, comment)

            response shouldEqual Right(CommentResponse.empty)
            verify(exactly = 1) { service.updateComment(board, token, id, comment) }
        }

        @Test
        @DisplayName("정상 작동 : comment 수정")
        fun `should update a comment from service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { updateCommentResponse.body() } returns SimpleResponseEntity("comment updated")
            every { updateCommentResponse.isSuccessful } returns true
            every { updateCommentCall.execute() } returns updateCommentResponse
            every { service.updateComment(board, token, id, comment) } returns updateCommentCall

            val response = networkRepository.updateComment(board, token, id, comment)

            response shouldEqual Right(CommentResponse("comment updated"))
            verify(exactly = 1) { service.updateComment(board, token, id, comment) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.updateComment(board, token, id, comment)
            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { updateCommentResponse.isSuccessful } returns false
            every { updateCommentCall.execute() } returns updateCommentResponse
            every { service.updateComment(board, token, id, comment) } returns updateCommentCall

            val response = networkRepository.updateComment(board, token, id, comment)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { updateCommentCall.execute() } returns updateCommentResponse
            every { service.updateComment(board, token, id, comment) } returns updateCommentCall

            val response = networkRepository.updateComment(board, token, id, comment)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }

    @Nested
    @DisplayName("Get Comment 테스트")
    inner class GetCommentTest {
        private val getCommentCall = mockk<Call<CommentEntity>>()
        private val getCommentResponse = mockk<Response<CommentEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty comment by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getCommentResponse.body() } returns null
            every { getCommentResponse.isSuccessful } returns true
            every { getCommentCall.execute() } returns getCommentResponse
            every { service.getComment(board, commentId) } returns getCommentCall

            val comment = networkRepository.getComment(board, commentId)

            comment shouldEqual Right(Comment.empty)
            verify(exactly = 1) { service.getComment(board, commentId) }
        }

        @Test
        @DisplayName("정상 작동 : comment 가져옴")
        fun `should get a comment from service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getCommentResponse.body() } returns CommentEntity(
                1,
                "comment",
                1,
                CommentEntity.User(1, "badge", "nickname", null)
            )
            every { getCommentResponse.isSuccessful } returns true
            every { getCommentCall.execute() } returns getCommentResponse
            every { service.getComment(board, commentId) } returns getCommentCall

            val comment = networkRepository.getComment(board, commentId)

            comment shouldEqual Right(Comment(1, "comment", 1, 1, "badge", "nickname", listOf()))
            verify(exactly = 1) { service.getComment(board, commentId) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `article service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val comment = networkRepository.getComment(board, commentId)
            comment shouldBeInstanceOf Either::class.java
            comment.isLeft shouldEqual true
            comment.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `article service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getCommentResponse.isSuccessful } returns false
            every { getCommentCall.execute() } returns getCommentResponse
            every { service.getComment(board, commentId) } returns getCommentCall

            val comment = networkRepository.getComment(board, commentId)

            comment shouldBeInstanceOf Either::class.java
            comment.isLeft shouldEqual true
            comment.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `article request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getCommentCall.execute() } returns getCommentResponse
            every { service.getComment(board, commentId) } returns getCommentCall

            val comment = networkRepository.getComment(board, commentId)

            comment shouldBeInstanceOf Either::class.java
            comment.isLeft shouldEqual true
            comment.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }
    }
}