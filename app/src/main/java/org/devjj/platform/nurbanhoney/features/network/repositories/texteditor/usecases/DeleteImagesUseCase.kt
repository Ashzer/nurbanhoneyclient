package org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.TextEditorRepository
import org.devjj.platform.nurbanhoney.features.ui.textedit.ImageResponse
import javax.inject.Inject

class DeleteImagesUseCase
@Inject constructor(
    private val repository: TextEditorRepository
) : UseCase<ImageResponse, DeleteImagesUseCase.Params>() {
    override suspend fun run(params: Params) = repository.deleteImages(params.token, params.uuid)

    data class Params(
        val token: String,
        val uuid: String,
    )
}