package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.usecases.*
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class TextEditorViewModel
@Inject constructor(
    private val uploadImage: UploadImageUseCase,
    private val uploadArticle: UploadArticleUseCase,
    private val uploadNurbanArticle: UploadNurbanArticleUseCase,
    private val deleteImages: DeleteImagesUseCase,
    private val modifyArticle: ModifyArticleUseCase,
    private val modifyNurbanArticle: ModifyNurbanArticleUseCase
) : BaseViewModel() {
    private val _imageURLs: MutableLiveData<List<URL>> = MutableLiveData()
    val imageURLs: LiveData<List<URL>> = _imageURLs
    private val _articleResponse: MutableLiveData<String> = MutableLiveData()
    val articleResponse: LiveData<String> = _articleResponse

    var board = Board.empty

    fun deleteImages(board: String, token: String, uuid: String) =
        deleteImages(DeleteImagesUseCase.Params(board, token, uuid), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleImageDeletion
            )
        }

    private fun handleImageDeletion(response: ImageResponse) {
        Log.d("text_check__", response.result)
    }


    fun uploadImage(
        board: String,
        token: String,
        uuid: MultipartBody.Part,
        image: MultipartBody.Part
    ) =
        uploadImage(UploadImageUseCase.Params(board, token, uuid, image), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleImageURL
            )
        }

    fun uploadArticle(
        board: String,
        token: String,
        title: String,
        uuid: String,
        lossCut: Long,
        thumbnail: String,
        content: String
    ) =
        uploadNurbanArticle(
            UploadNurbanArticleUseCase.Params(board, token, title, uuid, lossCut, thumbnail, content),
            viewModelScope
        ) {
            it.fold(
                ::handleFailure,
                ::handleUploading
            )
        }

    fun uploadArticle(
        board: String,
        token: String,
        title: String,
        uuid: String,
        thumbnail: String,
        content: String
    ) =
        uploadArticle(
            UploadArticleUseCase.Params(board, token, title, uuid, thumbnail, content),
            viewModelScope
        ) {
            it.fold(
                ::handleFailure,
                ::handleUploading
            )
        }

    fun modifyArticle(
        board: String,
        token: String,
        articleId: Int,
        thumbnail: String,
        title: String,
        lossCut: Long,
        content: String
    ) = modifyNurbanArticle(
        ModifyNurbanArticleUseCase.Params(board, token, articleId, thumbnail, title, lossCut, content),
        viewModelScope
    ) {
        it.fold(
            ::handleFailure,
            ::handleUploading
        )
    }

    fun modifyArticle(
        board: String,
        token: String,
        articleId: Int,
        thumbnail: String,
        title: String,
        content: String
    ) = modifyArticle(
        ModifyArticleUseCase.Params(board, token, articleId, thumbnail, title, content),
        viewModelScope
    ) {
        it.fold(
            ::handleFailure,
            ::handleUploading
        )
    }

    fun searchThumbnail(content: String) =
        "(http)[^>]*(?=\" alt)".toRegex().find(content)?.value.run {
            Log.d("string_check__", this.toString())
            this.toString()
        }

    private fun handleUploading(response: ArticleResponse) {
        //Toast.makeText(this , "글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        Log.d("text_check__", response.result)
        _articleResponse.postValue(response.result)
    }

    private fun handleImageURL(imageUploadResult: ImageUploadResult) {
        if (!imageUploadResult.url.equals("")) {
            var newImageURLs = mutableListOf<URL>()
            newImageURLs.addAll(_imageURLs.value?.toList() ?: listOf(imageUploadResult.url))
            if (!newImageURLs.contains(imageUploadResult.url)) {
                newImageURLs.add(imageUploadResult.url)
            }
            _imageURLs.postValue(newImageURLs)
        }
    }


}