package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.core.platform.BaseViewModel
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class TextEditorViewModel
@Inject constructor(
    private val uploadImage: UploadImageUseCase,
    private val uploadArticle: UploadArticleUseCase
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

    fun uploadArticle(
        token: String,
        title: String,
        uuid: String,
        lossCut: Long,
        thumbnail: String,
        content: String
    ) =
        uploadArticle(
            UploadArticleUseCase.Params(token, title, uuid, lossCut, thumbnail, content),
            viewModelScope
        ) {
            it.fold(
                ::handleFailure,
                ::handleUploading
            )
        }

    fun searchThumbnail(content: String) : String{

        for(index in 0 until (imageURLs.value?.size ?: 0)){
            var url = imageURLs.value?.get(index).toString()
            if(content.contains(url))
                return url
        }

        return ""
    }
    private fun handleUploading(uploadResult: UploadResult) {
        //Toast.makeText(this , "글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        Log.d("check__", uploadResult.toString())
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