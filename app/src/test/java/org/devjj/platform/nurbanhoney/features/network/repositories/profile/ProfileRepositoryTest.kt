package org.devjj.platform.nurbanhoney.features.network.repositories.profile

import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeIn
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.exception.Failure.NetworkConnection
import org.devjj.platform.nurbanhoney.core.exception.Failure.ServerError
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.functional.Either.Right
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.core.utils.LocalDateTimeUtils
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.network.entities.ProfileArticleEntity
import org.devjj.platform.nurbanhoney.features.network.entities.ProfileCommentEntity
import org.devjj.platform.nurbanhoney.features.network.entities.ProfileEntity
import org.devjj.platform.nurbanhoney.features.network.entities.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.ui.home.profile.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Response

class ProfileRepositoryTest : UnitTest() {

    lateinit var networkRepository: ProfileRepository.Network
    var networkHandler = mockk<NetworkHandler>()
    var service = mockk<ProfileService>()

    companion object {
        private const val token = "token"
        private const val offset = 0
        private const val limit = 10
        private const val id = 1
        private const val nickname = "nickname"
        private const val description = "description"
        private val insignia = listOf("ins1", "ins2")
    }

    @BeforeEach
    fun setUp() {
        networkRepository = ProfileRepository.Network(networkHandler, service)
    }

    @Nested
    @DisplayName("Get Profile 테스트")
    inner class GetProfileTest {
        private val getProfileCall = mockk<Call<ProfileEntity>>()
        private val getProfileResponse = mockk<Response<ProfileEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty profile by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfileResponse.body() } returns null
            every { getProfileResponse.isSuccessful } returns true
            every { getProfileCall.execute() } returns getProfileResponse
            every { service.getProfile(token) } returns getProfileCall

            val profile = networkRepository.getProfile(token)

            profile shouldEqual Right(Profile.empty)
            verify(exactly = 1) { service.getProfile(token) }
        }

        @Test
        @DisplayName("정상 작동 : 프로필 조회")
        fun `should get profile by token from service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfileResponse.body() } returns ProfileEntity(
                1,
                "kakao",
                "badge",
                "nickname",
                "description",
                0,
                null,
                null,
                0,
                0,
                ""
            )
            every { getProfileResponse.isSuccessful } returns true
            every { getProfileCall.execute() } returns getProfileResponse
            every { service.getProfile(token) } returns getProfileCall

            var profile = networkRepository.getProfile(token)

            profile shouldEqual Right(
                Profile(
                    1,
                    "kakao",
                    "badge",
                    "nickname",
                    "description",
                    0,
                    null,
                    null,
                    0,
                    0,
                    ""
                )
            )

            verify(exactly = 1) { service.getProfile(token) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `profile service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val profile = networkRepository.getProfile(token)
            profile shouldBeInstanceOf Either::class.java
            profile.isLeft shouldEqual true
            profile.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `profile service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfileResponse.isSuccessful } returns false
            every { getProfileCall.execute() } returns getProfileResponse
            every { service.getProfile(token) } returns getProfileCall

            val profile = networkRepository.getProfile(token)

            profile shouldBeInstanceOf Either::class.java
            profile.isLeft shouldEqual true
            profile.fold(
                { failure -> failure shouldBeInstanceOf ServerError::class.java },
                {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `profile request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfileCall.execute() } returns getProfileResponse
            every { service.getProfile(token) } returns getProfileCall

            val profile = networkRepository.getProfile(token)

            profile shouldBeInstanceOf Either::class.java
            profile.isLeft shouldEqual true
            profile.fold(
                { failure -> failure shouldBeInstanceOf ServerError::class.java },
                {})
        }
    }

    @Nested
    @DisplayName("Get My Articles 테스트")
    inner class GetMyArticlesTest {
        private val getProfilesArticleCall = mockk<Call<List<ProfileArticleEntity>>>()
        private val getProfilesArticleResponse = mockk<Response<List<ProfileArticleEntity>>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty list")
        fun `should return empty list by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfilesArticleResponse.body() } returns null
            every { getProfilesArticleResponse.isSuccessful } returns true
            every { getProfilesArticleCall.execute() } returns getProfilesArticleResponse
            every { service.getMyArticles(token, offset, limit) } returns getProfilesArticleCall

            val myArticles = networkRepository.getMyArticles(token, offset, limit)

            myArticles shouldEqual Right(emptyList<ProfileArticle>())
            verify(exactly = 1) { service.getMyArticles(token, offset, limit) }
        }

        @Test
        @DisplayName("정상 작동 : 유저 글 조회")
        fun `should get user article list by token from service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfilesArticleResponse.body() } returns listOf(
                ProfileArticleEntity(
                    1,
                    Board.empty,
                    String.empty(),
                    String.empty(),
                    0,
                    "2021-03-29T08:57:57.000Z"
                )
            )
            every { getProfilesArticleResponse.isSuccessful } returns true
            every { getProfilesArticleCall.execute() } returns getProfilesArticleResponse
            every { service.getMyArticles(token, offset, limit) } returns getProfilesArticleCall

            val profileArticles = networkRepository.getMyArticles(token, offset, limit)

            profileArticles shouldEqual Right(
                listOf(
                    ProfileArticle(
                        1,
                        Board.empty,
                        String.empty(),
                        String.empty(),
                        0,
                        LocalDateTimeUtils.parse("2021-03-29T08:57:57.000Z")
                    )
                )
            )

            verify(exactly = 1) { service.getMyArticles(token, offset, limit) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `profile service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val profileArticles = networkRepository.getMyArticles(token, offset, limit)
            profileArticles shouldBeInstanceOf Either::class.java
            profileArticles.isLeft shouldEqual true
            profileArticles.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `profile service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfilesArticleResponse.isSuccessful } returns false
            every { getProfilesArticleCall.execute() } returns getProfilesArticleResponse
            every { service.getMyArticles(token, offset, limit) } returns getProfilesArticleCall

            val profileArticles = networkRepository.getMyArticles(token, offset, limit)

            profileArticles shouldBeInstanceOf Either::class.java
            profileArticles.isLeft shouldEqual true
            profileArticles.fold(
                { failure -> failure shouldBeInstanceOf ServerError::class.java },
                {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `profile request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfilesArticleCall.execute() } returns getProfilesArticleResponse
            every { service.getMyArticles(token, offset, limit) } returns getProfilesArticleCall

            val profileArticles = networkRepository.getMyArticles(token, offset, limit)

            profileArticles shouldBeInstanceOf Either::class.java
            profileArticles.isLeft shouldEqual true
            profileArticles.fold(
                { failure -> failure shouldBeInstanceOf ServerError::class.java },
                {})
        }
    }

    @Nested
    @DisplayName("Get My Comments 테스트")
    inner class GetMyCommentsTest {
        private val getProfileCommentsCall = mockk<Call<List<ProfileCommentEntity>>>()
        private val getProfileCommentsResponse = mockk<Response<List<ProfileCommentEntity>>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty list")
        fun `should return empty list by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfileCommentsResponse.body() } returns null
            every { getProfileCommentsResponse.isSuccessful } returns true
            every { getProfileCommentsCall.execute() } returns getProfileCommentsResponse
            every { service.getMyComments(token, offset, limit) } returns getProfileCommentsCall

            val myComments = networkRepository.getMyComments(token, offset, limit)

            myComments shouldEqual Right(emptyList<ProfileComment>())

            verify(exactly = 1) { service.getMyComments(token, offset, limit) }
        }

        @Test
        @DisplayName("정상 작동 : 유저 댓글 조회")
        fun `should get user comment list by token from service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfileCommentsResponse.body() } returns listOf(
                ProfileCommentEntity(
                    1,
                    Board.empty,
                    "comment",
                    "2021-03-29T08:57:57.000Z",
                    ProfileCommentEntity.ArticleInfo(1, "title")
                )
            )
            every { getProfileCommentsResponse.isSuccessful } returns true
            every { getProfileCommentsCall.execute() } returns getProfileCommentsResponse
            every { service.getMyComments(token, offset, limit) } returns getProfileCommentsCall

            val profileComments = networkRepository.getMyComments(token, offset, limit)

            profileComments shouldEqual Right(
                listOf(
                    ProfileComment(
                        1,
                        "comment",
                        1,
                        LocalDateTimeUtils.parse("2021-03-29T08:57:57.000Z"),
                        Board.empty,
                        "title"
                    )
                )
            )

            verify(exactly = 1) { service.getMyComments(token, offset, limit) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `profile service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val profileComments = networkRepository.getMyComments(token, offset, limit)
            profileComments shouldBeInstanceOf Either::class.java
            profileComments.isLeft shouldEqual true
            profileComments.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `profile service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfileCommentsResponse.isSuccessful } returns false
            every { getProfileCommentsCall.execute() } returns getProfileCommentsResponse
            every { service.getMyComments(token, offset, limit) } returns getProfileCommentsCall

            val profileComments = networkRepository.getMyComments(token, offset, limit)

            profileComments shouldBeInstanceOf Either::class.java
            profileComments.isLeft shouldEqual true
            profileComments.fold(
                { failure -> failure shouldBeInstanceOf ServerError::class.java },
                {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `profile request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { getProfileCommentsCall.execute() } returns getProfileCommentsResponse
            every { service.getMyComments(token, offset, limit) } returns getProfileCommentsCall

            val profileComments = networkRepository.getMyComments(token, offset, limit)

            profileComments shouldBeInstanceOf Either::class.java
            profileComments.isLeft shouldEqual true
            profileComments.fold(
                { failure -> failure shouldBeInstanceOf ServerError::class.java },
                {})
        }
    }

    @Nested
    @DisplayName("Sign Out 테스트")
    inner class SignOutTest {
        private val signOutCall = mockk<Call<SimpleResponseEntity>>()
        private val signOutResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { signOutResponse.body() } returns null
            every { signOutResponse.isSuccessful } returns true
            every { signOutCall.execute() } returns signOutResponse
            every { service.signOut(token, id) } returns signOutCall

            val response = networkRepository.signOut(token, id)

            response shouldEqual Right(SignOutResponse.empty)

            verify(exactly = 1) { service.signOut(token, id) }
        }

        @Test
        @DisplayName("정상 작동 : 유저 로그아웃")
        fun `should logout user`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { signOutResponse.body() } returns SimpleResponseEntity("user logged out")
            every { signOutResponse.isSuccessful } returns true
            every { signOutCall.execute() } returns signOutResponse
            every { service.signOut(token, id) } returns signOutCall

            val response = networkRepository.signOut(token, id)

            response shouldEqual Right(SignOutResponse("user logged out"))

            verify(exactly = 1) { service.signOut(token, id) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `profile service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.signOut(token, id)

            response shouldBeInstanceOf  Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `profile service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { signOutResponse.isSuccessful } returns false
            every { signOutCall.execute() } returns signOutResponse
            every { service.signOut(token, id) } returns signOutCall

            val response = networkRepository.signOut(token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `profile request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { signOutCall.execute() } returns signOutResponse
            every { service.signOut(token, id) } returns signOutCall

            val response = networkRepository.signOut(token, id)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold(
                { failure -> failure shouldBeInstanceOf ServerError::class.java },
                {})
        }
    }

    @Nested
    @DisplayName("Edit profile 테스트")
    inner class EditProfileTest {
        private val editProfileCall = mockk<Call<SimpleResponseEntity>>()
        private val editProfileResponse = mockk<Response<SimpleResponseEntity>>()

        @Test
        @DisplayName("정상 작동 : 디폴트 리턴 값 == empty")
        fun `should return empty by default`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { editProfileResponse.body() } returns null
            every { editProfileResponse.isSuccessful } returns true
            every { editProfileCall.execute() } returns editProfileResponse
            every {
                service.editProfile(
                    token,
                    nickname,
                    description,
                    insignia
                )
            } returns editProfileCall

            val response = networkRepository.editProfile(token, nickname, description, insignia)

            response shouldEqual Right(EditProfileResponse.empty)
            verify(exactly = 1) { service.editProfile(token, nickname, description, insignia) }
        }

        @Test
        @DisplayName("정상 작동 : 프로파일 수정")
        fun `should send edit profile request through service`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { editProfileResponse.body() } returns SimpleResponseEntity("profile edited")
            every { editProfileResponse.isSuccessful } returns true
            every { editProfileCall.execute() } returns editProfileResponse
            every {
                service.editProfile(
                    token,
                    nickname,
                    description,
                    insignia
                )
            } returns editProfileCall

            val response = networkRepository.editProfile(token, nickname, description, insignia)

            response shouldEqual Right(EditProfileResponse("profile edited"))

            verify(exactly = 1) { service.editProfile(token, nickname, description, insignia) }
        }

        @Test
        @DisplayName("네트워크 연결 오류 : 네트워크 연결 실패")
        fun `profile service should return network failure when no connection`() {
            every { networkHandler.isNetworkAvailable() } returns false

            val response = networkRepository.editProfile(token, nickname, description, insignia)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
                {})
            verify { service wasNot Called }
        }

        @Test
        @DisplayName("서버 오류 : 서버 내부 오류")
        fun `profile service should return server error if no successful response`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { editProfileResponse.isSuccessful } returns false
            every { editProfileCall.execute() } returns editProfileResponse
            every { service.editProfile(token, nickname, description, insignia) } returns editProfileCall

            val response = networkRepository.editProfile(token, nickname, description, insignia)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
        }

        @Test
        @DisplayName("서버 오류 : 수신값이 비정상")
        fun `profile request should catch exceptions`() {
            every { networkHandler.isNetworkAvailable() } returns true
            every { editProfileCall.execute() } returns editProfileResponse
            every { service.editProfile(token, nickname, description, insignia) } returns editProfileCall

            val response = networkRepository.editProfile(token, nickname, description, insignia)

            response shouldBeInstanceOf Either::class.java
            response.isLeft shouldEqual true
            response.fold(
                { failure -> failure shouldBeInstanceOf ServerError::class.java },
                {})
        }
    }

}