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
    private val deleteComment: DeleteCommentUseCase,
    private val updateComment: UpdateCommentUseCase,
    private val getRatings : GetRatingsUseCase,
    private val getComment : GetCommentUseCase
) : BaseViewModel() {
    private val _articleId: MutableLiveData<Int> = MutableLiveData()
    val articleId: LiveData<Int> = _articleId
    private val _article: MutableLiveData<Article> = MutableLiveData()
    val article: LiveData<Article> = _article
    private val _ratingResponse : MutableLiveData<String> = MutableLiveData()
    val ratingResponse : LiveData<String> = _ratingResponse
    private val _ratings: MutableLiveData<Ratings> = MutableLiveData()
    val ratings: LiveData<Ratings> = _ratings
    private val _commentResponse: MutableLiveData<String> = MutableLiveData()
    val commentResponse: LiveData<String> = _commentResponse
    private val _commentsResponse: MutableLiveData<String> = MutableLiveData()
    val commentsResponse: LiveData<String> = _commentsResponse
    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData()
    val comments: LiveData<List<Comment>> = _comments
    var updatingCommentId = -1

    fun setArticleId(id: Int) {
        _articleId.postValue(id)
    }

    fun getArticle() = getArticle(
        GetArticleUseCase.Params(getToken(), articleId.value ?: -1), viewModelScope
    ) {
        it.fold(
            ::handleFailure,
            ::handleArticle
        )
    }

    private fun getToken(): String {
        return prefs.getString(
            R.string.prefs_nurban_token_key.toString(),
            ""
        ).toString()
    }

    fun postLike() {
        fun postLike(token: String, id: Int) =
            postLike(LikeUseCase.Params(token, id), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleRating
                )
            }

        postLike(
            getToken(),
            article.value?.id ?: -1
        )
    }

    fun postDislike() {
        fun postDislike(token: String, id: Int) =
            postDislike(DislikeUseCase.Params(token, id), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleRating
                )
            }

        postDislike(getToken(), article.value?.id ?: -1)
    }

    fun getRatings(){
        fun getRatings(articleId : Int) =
            getRatings(GetRatingsUseCase.Params(articleId),viewModelScope){
                it.fold(
                    ::handleFailure,
                    ::handleGetRatings
                )
            }
        getRatings(articleId?.value ?: -1)
    }

    private fun handleGetRatings(ratings: Ratings){
        _ratings.postValue(ratings)
    }

    fun postComment(comment: String) {
        fun postComment(token: String, comment: String, id: Int) =
            postComment(PostCommentUseCase.Params(token, comment, id), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handlePostComment
                )
            }
        postComment(getToken(), comment, article.value!!.id)
    }

    fun updateComment(comment: String, id : Int) {
        fun updateComment(token: String, id: Int, comment: String) =
            updateComment(UpdateCommentUseCase.Params(token, id, comment), viewModelScope){
                it.fold(
                    ::handleFailure,
                    ::handlePostComment
                )
            }
        updateComment(getToken(), id, comment)
        updatingCommentId = id
    }

    fun getComment(commentId : Int){
        fun getComment(commentId : Int) =
            getComment(GetCommentUseCase.Params(commentId),viewModelScope){
                it.fold(
                    ::handleFailure,
                    ::handleGetComment
                )
            }
        getComment(commentId)
    }


    fun deleteComment(id: Int) {
        fun deleteComment(token: String, id: Int, articleId: Int) =
            deleteComment(DeleteCommentUseCase.Params(token, id, articleId), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleCommentDelete
                )
            }
        deleteComment(getToken(), id, article.value?.id ?: -1)
    }

    fun getComments() {
        fun getComments(articleId: Int, offset: Int, limit: Int) =
            getComments(GetCommentsUseCase.Params(articleId, offset, limit), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleComments
                )
            }
        getComments(article.value!!.id, 0, 10)
    }

    fun initComments() {
        fun initComments(articleId: Int, offset: Int, limit: Int) =
            getComments(GetCommentsUseCase.Params(articleId, offset, limit), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleInitComments
                )
            }
        initComments(article.value!!.id, 0, 10)
    }


    private fun handleCommentDelete(response: CommentResponse) {
        _commentsResponse.postValue(response.result)
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

    private fun handleGetComment(comment : Comment){
        var newComments = _comments.value?.toMutableList() ?: mutableListOf()
        for(i in 0 until newComments.size){
            if(newComments[i].id==comment.id){
                newComments[i] = comment
                break
            }
        }
        _comments.postValue(newComments)
    }

    private fun handlePostComment(response: CommentResponse) {
        Log.d("Comment_check__", response.toString())
        _commentResponse.postValue(response.result)
    }


    private fun handleRating(response: RatingResponse) {
        // Log.d("result_like_check__",result.result)
        //_likes.postValue(Pair(result.likes, result.dislikes))
        _ratingResponse.postValue(response.result)
    }

    private fun handleArticle(article: Article) {
        Log.d("article_check__", article.toString())
        _article.postValue(article)
    }
}