package org.devjj.platform.nurbanhoney.features.ui.article

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.core.sharedpreference.Prefs
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases.*
import org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.usecases.DeleteArticleUseCase
import org.devjj.platform.nurbanhoney.features.ui.article.model.*
import org.devjj.platform.nurbanhoney.features.ui.textedit.ArticleResponse
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel
@Inject constructor(
    private val getArticle: GetArticleUseCase,
    private val postLike: LikeUseCase,
    private val unLike: UnLikeUseCase,
    private val postDislike: DislikeUseCase,
    private val unDislike: UnDislikeUseCase,
    private val postComment: PostCommentUseCase,
    private val getComments: GetCommentsUseCase,
    private val deleteComment: DeleteCommentUseCase,
    private val updateComment: UpdateCommentUseCase,
    private val getRatings: GetRatingsUseCase,
    private val getComment: GetCommentUseCase,
    private val deleteArticle: DeleteArticleUseCase
) : BaseViewModel() {
    private val _articleId: MutableLiveData<Int> = MutableLiveData()
    val articleId: LiveData<Int> = _articleId
    private val _article: MutableLiveData<Article> = MutableLiveData()
    val article: LiveData<Article> = _article
    private val _ratingResponse: MutableLiveData<String> = MutableLiveData()
    val ratingResponse: LiveData<String> = _ratingResponse
    private val _ratings: MutableLiveData<Ratings> = MutableLiveData()
    val ratings: LiveData<Ratings> = _ratings
    private val _commentResponse: MutableLiveData<String> = MutableLiveData()
    val commentResponse: LiveData<String> = _commentResponse
    private val _commentsResponse: MutableLiveData<String> = MutableLiveData()
    val commentsResponse: LiveData<String> = _commentsResponse
    private val _newComments: MutableLiveData<List<Comment>> = MutableLiveData()
    val newComments: LiveData<List<Comment>> = _newComments
    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData()
    val comments: LiveData<List<Comment>> = _comments
    var updatingCommentId = -1
    var offset = 0
    private val limit = 5
    lateinit var board: Board

    fun isAuthor() = article.value?.userId == Prefs.userId

    fun setArticleId(id: Int) {
        _articleId.postValue(id)
    }

    /*****************Loading*****************/
    fun getArticle() = getArticle(
        GetArticleUseCase.Params(board.address, Prefs.token, articleId.value ?: -1), viewModelScope
    ) {
        it.fold(
            ::handleFailure,
            ::handleArticle
        )
    }

    private fun handleArticle(article: Article) {
        _article.postValue(article)
        _ratings.postValue(Ratings(article.likes, article.dislikes, article.myRating))
    }

    /*****************Loading*****************/

    fun deleteArticle() {
        fun deleteArticle(board: String, token: String, articleId: Int, uuid: String) =
            deleteArticle(
                DeleteArticleUseCase.Params(board, token, articleId, uuid), viewModelScope
            ) {
                it.fold(
                    ::handleFailure,
                    ::handleDeletion
                )
            }

        deleteArticle(
            board.address,
            Prefs.token,
            articleId.value ?: -1,
            article.value?.uuid.toString()
        )
    }

    private fun handleDeletion(articleResponse: ArticleResponse) {

    }

    /*****************Ratings*****************/
    fun postLike() {
        fun postLike(board: String, token: String, id: Int) =
            postLike(LikeUseCase.Params(board, token, id), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleRating
                )
            }

        postLike(
            board.address,
            Prefs.token,
            article.value?.id ?: -1
        )
    }

    fun unLike() {
        fun unLike(board: String, token: String, id: Int) =
            unLike(UnLikeUseCase.Params(board, token, id), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleRating
                )
            }
        unLike(
            board.address,
            Prefs.token,
            article.value?.id ?: -1
        )
    }

    fun postDislike() {
        fun postDislike(board: String, token: String, id: Int) =
            postDislike(DislikeUseCase.Params(board, token, id), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleRating
                )
            }

        postDislike(board.address, Prefs.token, article.value?.id ?: -1)
    }

    fun unDislike() {
        fun unDislike(board: String, token: String, id: Int) =
            unDislike(UnDislikeUseCase.Params(board, token, id), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleRating
                )
            }

        unDislike(board.address, Prefs.token, article.value?.id ?: -1)
    }

    private fun handleRating(response: RatingResponse) {
        Log.d("rating_check__", response.result)
        _ratingResponse.postValue(response.result)
    }
    /*****************Ratings*****************/

    /*****************Refresh Rating*****************/
    fun getRatings() {
        fun getRatings(board: String, token: String, articleId: Int) =
            getRatings(GetRatingsUseCase.Params(board, token, articleId), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleGetRatings
                )
            }
        getRatings(board.address, Prefs.token, articleId?.value ?: -1)
    }

    private fun handleGetRatings(ratings: Ratings) {
        Log.d("rating_check__", ratings.toString())
        _ratings.postValue(ratings)
    }
    /*****************Refresh Rating*****************/

    /*****************Post Comment*****************/
    fun postComment(comment: String) {
        fun postComment(board: String, token: String, comment: String, id: Int) =
            postComment(PostCommentUseCase.Params(board, token, comment, id), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handlePostComment
                )
            }
        postComment(board.address, Prefs.token, comment, article.value!!.id)
    }

    private fun handlePostComment(response: CommentResponse) {
        initComments()
    }

    /*****************Post Comment*****************/

    fun updateComment(comment: String, id: Int) {
        fun updateComment(board: String, token: String, id: Int, comment: String) =
            updateComment(UpdateCommentUseCase.Params(board, token, id, comment), viewModelScope) {
                it.fold(
                    ::handleFailure,
                    ::handleUpdateComment
                )
            }
        updateComment(board.address, Prefs.token, id, comment)
        updatingCommentId = id
    }

    private fun handleUpdateComment(response: CommentResponse) {
        //Log.d("Comment_check__", response.toString())
        _commentResponse.postValue(response.result)
    }

    //Todo: 확인
//    fun getComment(commentId: Int) {
//        fun getComment(board: String, commentId: Int) =
//            getComment(GetCommentUseCase.Params(board, commentId), viewModelScope) {
//                it.fold(
//                    ::handleFailure,
//                    ::handleGetComment
//                )
//            }
//        getComment(board.address, commentId)
//    }
//
//    private fun handleGetComment(comment: Comment) {
//        var newComments = _newComments.value?.toMutableList() ?: mutableListOf()
//        for (i in 0 until newComments.size) {
//            if (newComments[i].id == comment.id) {
//                newComments[i] = comment
//                break
//            }
//        }
//        _newComments.postValue(newComments)
//    }

    fun deleteComment(id: Int) {
        fun deleteComment(board: String, token: String, id: Int, articleId: Int) =
            deleteComment(
                DeleteCommentUseCase.Params(board, token, id, articleId),
                viewModelScope
            ) {
                it.fold(
                    ::handleFailure,
                    ::handleCommentDelete
                )
            }
        deleteComment(board.address, Prefs.token, id, article.value?.id ?: -1)
    }

    private fun handleCommentDelete(response: CommentResponse) {
        _commentsResponse.postValue(response.result)
    }

    private fun getComments(board: String, articleId: Int, offset: Int, limit: Int) =
        getComments(GetCommentsUseCase.Params(board, articleId, offset, limit), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleComments
            )
        }


    fun getComments() {
        getComments(board.address, article.value!!.id, offset, limit)
        offset += limit
    }

    private fun handleComments(comments: List<Comment>) {
        comments.forEach {
            Log.d("Comments_check__", it.comment.toString())
        }
        Log.d("Comments_check__","=================================================")
        var totalComments = _comments.value?.toMutableList() ?: mutableListOf()
        totalComments?.addAll(comments)
        _comments.postValue(totalComments)
        _newComments.postValue(comments)
    }

    fun initComments() {
        fun initComments(board: String, articleId: Int, offset: Int, limit: Int) =
            getComments(
                GetCommentsUseCase.Params(board, articleId, offset, limit),
                viewModelScope
            ) {
                it.fold(
                    ::handleFailure,
                    ::handleInitComments
                )
            }
        initComments(board.address, article.value!!.id, 0, limit*2)
        offset += limit*2
    }

    private fun handleInitComments(comments: List<Comment>) {
        _newComments.postValue(comments)
        _comments.postValue(comments)
    }

}