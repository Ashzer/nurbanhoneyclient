package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.extension.close
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class TextEditorViewModel
@Inject constructor(
    private val uploadImage: UploadImageUseCase,
    private val uploadArticle : UploadArticleUseCase
) : BaseViewModel() {
    private val _imageURLs: MutableLiveData<List<URL>> = MutableLiveData()
    val imageURLs: LiveData<List<URL>> = _imageURLs

    fun uploadImage(token: String, uuid: MultipartBody.Part, image: MultipartBody.Part) =
        uploadImage(UploadImageUseCase.Params(token, uuid, image), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleImageURL
            )
        }

    fun uploadArticle(token: String, title: String, content: String, uuid: String) =
        uploadArticle(UploadArticleUseCase.Params(token,title,content,uuid), viewModelScope){
            it.fold(
                ::handleFailure,
                ::handleUploading
            )
        }

    private fun handleUploading(uploadResult: UploadResult){
        //Toast.makeText(this , "글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
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