package org.devjj.platform.nurbanhoney.features.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class NurbanHoneyViewModel
@Inject constructor(
    private val getArticles: GetArticlesUseCase
) :
    BaseViewModel() {
        private val _articles : MutableLiveData<List<NurbanHoneyArticle>> = MutableLiveData()
        val articles : LiveData<List<NurbanHoneyArticle>> = _articles

    fun getArticles(token : String , offset : Int, limit : Int) =
        getArticles(GetArticlesUseCase.Params(token, offset, limit) , viewModelScope){
            it.fold(
                ::handleFailure,
                ::handArticles
            )
        }

    private fun handArticles(articles : List<NurbanHoneyArticle>){
        articles.forEach {
            Log.d("get_check__",it.author)
        }
    }
}