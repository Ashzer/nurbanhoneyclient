package org.devjj.platform.nurbanhoney.features.network

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileService
@Inject constructor(retrofit: Retrofit) : ProfileApi {
    private val profileApi by lazy { retrofit.create(ProfileApi::class.java) }

    override fun getProfile(token: String) = profileApi.getProfile(token)
    override fun getMyArticles(
        token: String,
        offset: Int,
        limit: Int
    ) = profileApi.getMyArticles(token, offset, limit)

    override fun getMyComments(token: String, offset: Int, limit: Int) =
        profileApi.getMyComments(token, offset, limit)

    override fun signOut(token: String, id: Int) = profileApi.signOut(token, id)

    override fun editProfile(
        token: String,
        nickname: String,
        description: String,
        insignia: List<String>
    ) = profileApi.editProfile(token, nickname, description, insignia)
}