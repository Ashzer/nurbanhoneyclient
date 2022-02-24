package org.devjj.platform.nurbanhoney.features.ui.home.profile

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases.EditProfileUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val prefs: SharedPreferences,
    private val getProfile: GetProfileUseCase,
    private val editProfile: EditProfileUseCase
) : BaseViewModel() {
    private val _profile: MutableLiveData<Profile> = MutableLiveData()
    val profile: LiveData<Profile> = _profile

    private val _insigniaOwn : MutableLiveData<List<String>> = MutableLiveData()
    val insigniaOwn : LiveData<List<String>> = _insigniaOwn

    private fun getToken(): String {
        return prefs.getString(
            prefsNurbanTokenKey,
            ""
        ).toString()
    }

    /*****************Loading*****************/
    /*
    fun getArticle() = getArticle(
        GetArticleUseCase.Params(getToken(), articleId.value ?: -1), viewModelScope
    ) {
        it.fold(
            ::handleFailure,
            ::handleArticle
        )
    }

    private fun handleArticle(article: Article) {
        Log.d("article_check__", article.toString())
        _article.postValue(article)
        _ratings.postValue(Ratings(article.likes, article.dislikes, article.myRating))
    }
*/
    fun getProfile() = getProfile(
        GetProfileUseCase.Params(getToken()), viewModelScope
    ) {
        it.fold(
            ::handleFailure,
            ::handleProfile
        )
    }

    private fun handleProfile(profile: Profile) {
        Log.d("profile_check__", profile.toString())
        _profile.postValue(profile)
        _insigniaOwn.postValue(profile.insigniaOwn)

    }

    /*****************Loading*****************/

    fun editProfile(nickname: String, description : String, insignia : List<String>) {
        fun editProfile(
            token: String,
            nickname: String,
            description: String,
            insignia: List<String>
        ) =
            editProfile(
                EditProfileUseCase.Params(token, nickname, description, insignia),
                viewModelScope
            ) {
                it.fold(
                    ::handleFailure,
                    ::handleProfileUpdate
                )
            }

        editProfile(getToken(), nickname, description, insignia)
    }

    private fun handleProfileUpdate(result: EditProfileResponse?) {
        Log.d("profile_check__", result.toString())
        getProfile()
    }


}