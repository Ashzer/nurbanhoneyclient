package org.devjj.platform.nurbanhoney.features.ui.home

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
class NurbanHoneyViewModel
@Inject constructor(
    private val getArticles: GetArticlesUseCase,
    private val prefs: SharedPreferences
) :
    BaseViewModel() {
    private val _articles: MutableLiveData<List<NurbanHoneyArticle>> = MutableLiveData()
    val articles: LiveData<List<NurbanHoneyArticle>> = _articles
    var offset = 0
    private val limit = 10
    private fun _getArticles(token: String, flag: Int, offset: Int, limit: Int) =
        getArticles(GetArticlesUseCase.Params(token, flag, offset, limit), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handArticles
            )
        }

    fun getArticles() {
        _getArticles(
            prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
            flag = 0,
            offset,
            limit
        )
        offset += limit
    }

    private fun handArticles(newArticles: List<NurbanHoneyArticle>) {
        var newArticleList = _articles.value?.toMutableList() ?: mutableListOf()
        newArticleList?.addAll(newArticles)
        _articles.postValue(newArticleList)

        Log.d(
            "prefs_check__",
            prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString()
        )
    }
}