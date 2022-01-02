package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.core.platform.DataLoadController
import org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases.GetArticlesUseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.board.usecases.GetBoardsUseCase
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import javax.inject.Inject

@HiltViewModel
class BoardViewModel
@Inject constructor(
    private val getArticles: GetArticlesUseCase,
    private val getBoards: GetBoardsUseCase,
    private val prefs: SharedPreferences
) :
    BaseViewModel() {
    private val _articles: MutableLiveData<List<NurbanHoneyArticle>> = MutableLiveData()
    val articles: LiveData<List<NurbanHoneyArticle>> = _articles
    private var offset = 0
    private val limit = 5
    var board = ""
    var boardName = "너반꿀"
    var boardId = 0
    private val _boards: MutableLiveData<List<Board>> = MutableLiveData()
    val boards: LiveData<List<Board>> = _boards

    val controller = DataLoadController(
        initialize = { initArticles() },
        getNext = { getNext() },
        loadNext = { loadNext() }
    )

    fun setCurrentBoard(id: Int) {
        boardId = id
        boardName = boards.value?.get(boardId)?.name ?: ""
        board = boards.value?.get(boardId)?.address ?: ""
    }

    private fun initArticles() {
        fun initArticles(board: String, flag: Int, offset: Int, limit: Int) =
            getArticles(GetArticlesUseCase.Params(board, flag, offset, limit), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleInitArticles
                )
            }

        initArticles(board, flag = 0, 0, limit)
        offset = limit
    }

    private fun handleInitArticles(articles: List<NurbanHoneyArticle>) {
        _articles.postValue(articles)
    }

    fun getNext() {

    }

    fun loadNext(): List<NurbanHoneyArticle> {
        return emptyList()
    }

    fun getArticles() {
        fun getArticles(board: String, flag: Int, offset: Int, limit: Int) =
            getArticles(GetArticlesUseCase.Params(board, flag, offset, limit), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handArticles
                )
            }
        getArticles(board, flag = 0, offset, limit)
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

    fun getBoards() =
        getBoards(UseCase.None(), viewModelScope) {
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