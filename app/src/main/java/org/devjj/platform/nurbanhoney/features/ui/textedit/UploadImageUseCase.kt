package org.devjj.platform.nurbanhoney.features.ui.textedit

import okhttp3.MultipartBody
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import javax.inject.Inject

class UploadImageUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<ImageUploadResult, UploadImageUseCase.Params>() {
    override suspend fun run(params: Params) = repository.uploadImage(params.token,params.uuid,params.image)
    data class Params(val token : String, val uuid : MultipartBody.Part, val image: MultipartBody.Part)
}