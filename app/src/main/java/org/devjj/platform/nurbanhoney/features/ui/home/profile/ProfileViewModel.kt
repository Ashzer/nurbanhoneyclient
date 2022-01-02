 package org.devjj.platform.nurbanhoney.features.ui.home.profile

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val prefs: SharedPreferences,
    private val getProfile: GetProfileUseCase
) : BaseViewModel() {
    private val _profile: MutableLiveData<Profile> = MutableLiveData()
    val profile: LiveData<Profile> = _profile

    private fun getToken(): String {
        return prefs.getString(
            R.string.prefs_nurban_token_key.toString(),
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
    ){
        it.fold(
            ::handleFailure,
            ::handleProfile
        )
    }

    private fun handleProfile(profile: Profile) {
        Log.d("profile_check__", profile.toString())
        _profile.postValue(profile)
    }

    /*****************Loading*****************/



}