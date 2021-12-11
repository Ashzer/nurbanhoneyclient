package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.core.platform.DataLoadController
import org.devjj.platform.nurbanhoney.features.ui.home.GetArticlesUseCase
import javax.inject.Inject

@HiltViewModel
class NurbanHoneyViewModel
@Inject constructor(
    private val getArticles: GetArticlesUseCase,
    private val prefs: SharedPreferences
) : BaseViewModel() {
    private val _articles: MutableLiveData<List<NurbanHoneyArticle>> = MutableLiveData()
    val articles: LiveData<List<NurbanHoneyArticle>> = _articles
    var offset = 0
    private val limit = 10

    val controller = DataLoadController(
        initialize = {initArticles()},
        getNext = {getNext()},
        loadNext = {loadNext()}
    )

    private fun getArticles(flag: Int, offset: Int, limit: Int) =
        getArticles(GetArticlesUseCase.Params(flag, offset, limit), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handArticles
            )
        }

    private fun initArticles(){
        getArticles(
            flag = 0,
            0,
            limit
        )
    }

    fun getNext(){

    }

    fun loadNext() : List<NurbanHoneyArticle>{
        return emptyList()
    }

    fun getArticles() {
        getArticles(
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