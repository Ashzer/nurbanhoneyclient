package org.devjj.platform.nurbanhoney.features.network

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileService
@Inject constructor(retrofit: Retrofit) : ProfileApi {
    private val profileApi by lazy { retrofit.create(ProfileApi::class.java) }

    override fun getProfile(token: String) = profileApi.getProfile(token)
}