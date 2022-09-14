package org.devjj.platform.nurbanhoney.features.ui.home.boards.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.core.platform.LazyLoadHelper
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases.GetArticlesUseCase
import javax.inject.Inject

@HiltViewModel
class BoardViewModel
@Inject constructor(
    private val getArticles: GetArticlesUseCase,
    //private val lazyLoadHelper: LazyLoadHelper<ArticleItem>
) : BaseViewModel() {
    private val _articles: MutableLiveData<List<ArticleItem>> = MutableLiveData()
    val articles: LiveData<List<ArticleItem>> = _articles
    private val _newArticles: MutableLiveData<List<ArticleItem>> = MutableLiveData()
    val newArticles: LiveData<List<ArticleItem>> = _newArticles

    private var offset = 0
    private val limit = 10

    lateinit var board: Board

    fun clear() {
        _articles.postValue(listOf())
        _newArticles.postValue(listOf())
        offset = 0
    }

    fun initArticles() {
        fun initArticles(board: String, flag: Int, offset: Int, limit: Int) =
            getArticles(GetArticlesUseCase.Params(board, flag, offset, limit), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleInitArticles
                )
            }

        initArticles(board.address, flag = 0, 0, limit)
        offset = limit
    }

    private fun handleInitArticles(articleItems: List<ArticleItem>) {
        _articles.postValue(articleItems)
        _newArticles.postValue(articleItems)
    }

    fun getArticles() {
        fun getArticles(board: String, flag: Int, offset: Int, limit: Int) =
            getArticles(GetArticlesUseCase.Params(board, flag, offset, limit), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handArticles
                )
            }
        getArticles(board.address, flag = 0, offset, limit)
        offset += limit
    }

    private fun handArticles(newArticleItems: List<ArticleItem>) {
        val newArticleList = _articles.value?.toMutableList() ?: mutableListOf()
        val adder = getListNotContained(newArticleItems, _articles.value.orEmpty())
        newArticleList.addAll(adder)
        _articles.postValue(newArticleList)
        _newArticles.postValue(newArticleItems)
    }

    private fun getListNotContained(newList: List<ArticleItem>, oldList: List<ArticleItem>) =
        newList.filterNot { oldList.contains(it) }.toList()

}