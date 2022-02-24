package org.devjj.platform.nurbanhoney.features.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.interactor.UseCase.None
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.network.repositories.board.usecases.GetBoardsUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    private val getBoards: GetBoardsUseCase
) : BaseViewModel() {

}