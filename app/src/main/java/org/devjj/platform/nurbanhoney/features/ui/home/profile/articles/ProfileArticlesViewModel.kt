package org.devjj.platform.nurbanhoney.features.ui.home.profile.articles

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases.GetProfileArticlesUseCase
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileArticle
import javax.inject.Inject

@HiltViewModel
class ProfileArticlesViewModel
@Inject constructor(
    private val prefs: SharedPreferences,
    val getArticles: GetProfileArticlesUseCase,
) : BaseViewModel() {
    private val _articles: MutableLiveData<List<ProfileArticle>> = MutableLiveData()
    val articles: LiveData<List<ProfileArticle>> = _articles

    var offset = 0
    var limit = 10

    private fun getToken() = prefs.getString(
        prefsNurbanTokenKey,
        ""
    ).toString()

    fun getArticles() {
        fun getArticles(token: String, offset: Int, limit: Int) =
            getArticles(GetProfileArticlesUseCase.Params(token, offset, limit), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleArticles
                )
            }
        Log.d("token_check__", getToken())
        getArticles(getToken(), offset, limit)
        offset += limit
    }

    private fun handleArticles(articles: List<ProfileArticle>?) {
        var list = _articles.value?.toMutableList() ?: mutableListOf()
        list.addAll(articles?.toList().orEmpty())
        _articles.postValue(articles)

        articles?.forEach {
            Log.d("profile_check__", it.toString())
        }
    }

}