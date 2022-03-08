package org.devjj.platform.nurbanhoney.features.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.features.network.repositories.board.usecases.GetBoardsUseCase
import org.devjj.platform.nurbanhoney.features.Board
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getBoards: GetBoardsUseCase
) : BaseViewModel() {
    private val _boards: MutableLiveData<List<Board>> = MutableLiveData()
    val boards: LiveData<List<Board>> = _boards

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