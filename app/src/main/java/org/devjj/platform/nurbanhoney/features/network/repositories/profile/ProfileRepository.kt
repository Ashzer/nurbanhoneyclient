package org.devjj.platform.nurbanhoney.features.network.repositories.profile

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.entities.ProfileEntity
import org.devjj.platform.nurbanhoney.features.network.entities.SimpleResponseEntity
import org.devjj.platform.nurbanhoney.features.ui.home.profile.*
import javax.inject.Inject

interface ProfileRepository {
    fun getProfile(token: String): Either<Failure, Profile>
    fun getMyArticles(token: String, offset: Int, limit: Int): Either<Failure, List<ProfileArticle>>
    fun getMyComments(token: String, offset: Int, limit: Int): Either<Failure, List<ProfileComment>>
    fun signOut(token: String, id: Int): Either<Failure, SignOutResponse>
    fun editProfile(
        token: String,
        nickname: String,
        description: String,
        insignia: List<String>
    ): Either<Failure, EditProfileResponse>

    class Network
    @Inject
    constructor(
        private val networkHandler: NetworkHandler,
        private val profileService: ProfileService
    ) : ProfileRepository {
        override fun getProfile(token: String): Either<Failure, Profile> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    profileService.getProfile(token),
                    { it.toProfile() },
                    ProfileEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getMyArticles(
            token: String,
            offset: Int,
            limit: Int
        ): Either<Failure, List<ProfileArticle>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    profileService.getMyArticles(token, offset, limit),
                    { it.map { ProfileArticleEntity -> ProfileArticleEntity.toProfileArticle() } },
                    listOf()
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getMyComments(
            token: String,
            offset: Int,
            limit: Int
        ): Either<Failure, List<ProfileComment>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    profileService.getMyComments(token, offset, limit),
                    { it.map { ProfileCommentEntity -> ProfileCommentEntity.toProfileComment() } },
                    listOf()
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun signOut(token: String, id: Int): Either<Failure, SignOutResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    profileService.signOut(token, id),
                    { it.toSignOutResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun editProfile(
            token: String,
            nickname: String,
            description: String,
            insignia: List<String>
        ): Either<Failure, EditProfileResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> networkHandler.request(
                    profileService.editProfile(token, nickname, description, insignia),
                    { it.toEditProfileResponse() },
                    SimpleResponseEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
    }
}