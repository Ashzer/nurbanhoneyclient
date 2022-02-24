package org.devjj.platform.nurbanhoney.features.ui.home.boards.model

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.core.platform.DataLoadController
import org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases.GetArticlesUseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.board.usecases.GetBoardsUseCase
import org.devjj.platform.nurbanhoney.features.Board
import javax.inject.Inject

@HiltViewModel
class BoardViewModel
@Inject constructor(
    private val getArticles: GetArticlesUseCase,
    private val prefs: SharedPreferences,
    private val getBoards: GetBoardsUseCase
) : BaseViewModel() {
    private val _articles: MutableLiveData<List<ArticleItem>> = MutableLiveData()
    val articles: LiveData<List<ArticleItem>> = _articles
    private val _newArticles: MutableLiveData<List<ArticleItem>> = MutableLiveData()
    val newArticles: LiveData<List<ArticleItem>> = _newArticles
    private val _boards: MutableLiveData<List<Board>> = MutableLiveData()
    val boards: LiveData<List<Board>> = _boards

    private var offset = 0
    private val limit = 10

    //TODO("첫 실행화면을 인기게시판으로 바꾼후에는 lateinit var로 변경")
    var board: Board = Board(0,"너반꿀","nurban")



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
        var newArticleList = _articles.value?.toMutableList() ?: mutableListOf()
        newArticleList?.addAll(newArticleItems)
        _articles.postValue(newArticleList)
        _newArticles.postValue(newArticleItems)
    }


    fun getBoards() =
        getBoards(org.devjj.platform.nurbanhoney.core.interactor.UseCase.None(), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleBoards
            )
        }

    private fun handleBoards(boards: List<Board>) {
        _boards.postValue(boards)
        boards.forEach {
            Log.d("boards_check__", it.toString())
        }
    }

}