package org.devjj.platform.nurbanhoney.features.ui.home.profile.comments

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.core.sharedpreference.Prefs
import org.devjj.platform.nurbanhoney.features.network.repositories.board.usecases.GetBoardsUseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.profile.usecases.GetProfileCommentsUseCase
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileComment
import org.devjj.platform.nurbanhoney.features.Board
import javax.inject.Inject

@HiltViewModel
class ProfileCommentsViewModel
@Inject constructor(
    val getComments: GetProfileCommentsUseCase,
    val getBoards: GetBoardsUseCase
) : BaseViewModel() {
    private val _comments: MutableLiveData<List<ProfileComment>> = MutableLiveData()
    val comments: LiveData<List<ProfileComment>> = _comments
    private val _boards: MutableLiveData<List<Board>> = MutableLiveData()
    val boards: LiveData<List<Board>> = _boards

    fun getComments() {
        fun getComments(token: String, offset: Int, limit: Int) =
            getComments(GetProfileCommentsUseCase.Params(token, offset, limit), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleComments
                )
            }

        getComments(Prefs.token, 0, 5)
    }

    private fun handleComments(comments: List<ProfileComment>?) {
        var list = _comments.value?.toMutableList() ?: mutableListOf()
        list.addAll(comments?.toList().orEmpty())
        _comments.postValue(list)
    }

}