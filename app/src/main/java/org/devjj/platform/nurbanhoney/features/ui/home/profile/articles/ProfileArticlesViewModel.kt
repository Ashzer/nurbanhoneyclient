package org.devjj.platform.nurbanhoney.features.ui.home.profile.articles

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.features.network.repositories.board.usecases.GetBoardsUseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases.GetProfileArticlesUseCase
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileArticle
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import javax.inject.Inject

@HiltViewModel
class ProfileArticlesViewModel
@Inject constructor(
    private val prefs: SharedPreferences,
    val getArticles: GetProfileArticlesUseCase,
    private val getBoards: GetBoardsUseCase,
) : BaseViewModel() {
    private val _articles : MutableLiveData<List<ProfileArticle>> = MutableLiveData()
    val articles : LiveData<List<ProfileArticle>> = _articles
    private val _boards: MutableLiveData<List<Board>> = MutableLiveData()
    val boards: LiveData<List<Board>> = _boards

    private fun getToken() = prefs.getString(
        prefsNurbanTokenKey,
        ""
    ).toString()

    fun getArticles(){
        fun getArticles(token : String, offset : Int, limit : Int)=
            getArticles(GetProfileArticlesUseCase.Params(token, offset, limit),viewModelScope){
                it.fold(
                    ::handleFailure,
                    ::handleArticles
                )
            }
        Log.d("token_check__",getToken())
        getArticles(getToken(),0,5)
    }

   private fun handleArticles(articles : List<ProfileArticle>?){
       var list = _articles.value?.toMutableList() ?: mutableListOf()
       articles?.toMutableList()?.map { it.flag = getBoardName(it.flag) }
       list.addAll(articles?.toList().orEmpty())
        _articles.postValue(articles)

       articles?.forEach {
           Log.d("profile_check__", it.toString())
       }
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

    fun getBoardName(address: String): String{
        boards.value?.forEach {
            if(it.address == address){
                return it.name
            }
        }
        return address
    }

    fun getBoard(address: String): Board{
        boards.value?.forEach {
            if(it.address == address){
                return it
            }
        }
        return Board.empty
    }
}