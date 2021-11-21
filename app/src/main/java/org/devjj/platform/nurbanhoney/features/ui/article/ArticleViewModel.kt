package org.devjj.platform.nurbanhoney.features.ui.article

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
class ArticleViewModel
@Inject constructor(
    private val prefs: SharedPreferences,
    private val getArticle: GetArticleUseCase,
    private val postLike: LikeUseCase,
    private val postDislike: DislikeUseCase,
    private val postComment: PostCommentUseCase,
    private val getComments: GetCommentsUseCase,
    private val deleteComment: DeleteCommentUseCase
) : BaseViewModel() {
    private val _article: MutableLiveData<Article> = MutableLiveData()
    val article: LiveData<Article> = _article
    private val _likes: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    val likes: LiveData<Pair<Int, Int>> = _likes
    private val _commentResponse: MutableLiveData<String> = MutableLiveData()
    val commentResponse: LiveData<String> = _commentResponse
    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData()
    val comments: LiveData<List<Comment>> = _comments

    fun getArticle(id: Int) = getArticle(
        GetArticleUseCase.Params(
            prefs.getString(
                R.string.prefs_nurban_token_key.toString(),
                ""
            ).toString(), id
        ), viewModelScope
    ) {
        it.fold(
            ::handleFailure,
            ::handleArticle
        )
    }

    private fun _postLike(token: String, id: Int) =
        postLike(LikeUseCase.Params(token, id), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleLike
            )
        }

    private fun _postDislike(token: String, id: Int) =
        postDislike(DislikeUseCase.Params(token, id), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleLike
            )
        }

    private fun _postComment(token: String, comment: String, id: Int) =
        postComment(PostCommentUseCase.Params(token, comment, id), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handlePostComment
            )
        }

    private fun _getComments(articleId: Int, offset: Int, limit: Int) =
        getComments(GetCommentsUseCase.Params(articleId, offset, limit), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleComments
            )
        }

    private fun _deleteComment(token : String, id : Int) =
        deleteComment(DeleteCommentUseCase.Params(token, id) , viewModelScope){
            it.fold(
                ::handleFailure,
                ::handleCommentDelete
            )
        }

    fun deleteComment(id : Int){
        _deleteComment(
            prefs.getString(
                R.string.prefs_nurban_token_key.toString(),
                ""
            ).toString(),
            id
        )
    }

    fun getComments() {
        _getComments(article.value!!.id, 0, 10)
    }

    fun initComments() {
        _initComments(article.value!!.id, 0, 10)
    }

    private fun _initComments(articleId: Int, offset: Int, limit: Int) =
        getComments(GetCommentsUseCase.Params(articleId, offset, limit), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleInitComments
            )
        }

    private fun handleCommentDelete(response : CommentResponse){
        _commentResponse.postValue(response.result)
    }

    private fun handleInitComments(comments: List<Comment>) {
        _comments.postValue(comments)
    }

    private fun handleComments(comments: List<Comment>) {
        comments.forEach {
            Log.d("Comment_check__", it.toString())
        }
        var newComments = _comments.value?.toMutableList() ?: mutableListOf()
        newComments?.addAll(comments)
        _comments.postValue(newComments)
    }

    private fun handlePostComment(response: CommentResponse) {
        Log.d("Comment_check__", response.toString())
        _commentResponse.postValue(response.result)
    }

    fun postComment(comment: String) {
        _postComment(
            prefs.getString(
                R.string.prefs_nurban_token_key.toString(),
                ""
            ).toString(), comment, article.value!!.id
        )
    }

    fun postLike() {
        _postLike(
            prefs.getString(
                R.string.prefs_nurban_token_key.toString(),
                ""
            ).toString(),
            article.value?.id ?: -1
        )
    }

    fun postDislike() {
        _postDislike(
            prefs.getString(
                R.string.prefs_nurban_token_key.toString(),
                ""
            ).toString(),
            article.value?.id ?: -1
        )
    }

    private fun handleLike(result: LikeResult) {
        // Log.d("result_like_check__",result.result)
        _likes.postValue(Pair(result.likes, result.dislikes))
    }

    private fun handleArticle(article: Article) {
        Log.d("article_check__", article.toString())
        _article.postValue(article)
    }
}