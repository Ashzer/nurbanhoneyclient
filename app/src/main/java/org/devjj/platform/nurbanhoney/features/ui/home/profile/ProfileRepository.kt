package org.devjj.platform.nurbanhoney.features.ui.home.profile

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.ProfileService
import org.devjj.platform.nurbanhoney.features.ui.home.ProfileEntity
import javax.inject.Inject

interface ProfileRepository {
    fun getProfile(token: String): Either<Failure, Profile>

    class Network
    @Inject constructor(
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
        
    }
}