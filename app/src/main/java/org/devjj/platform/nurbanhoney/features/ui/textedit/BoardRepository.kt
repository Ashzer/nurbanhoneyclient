package org.devjj.platform.nurbanhoney.features.ui.textedit

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.network.BoardService
import java.util.*
import javax.inject.Inject

interface BoardRepository {
    fun uploadWriting(token : String , title : String , content : String , uuid: String) : Either<Failure, UploadResult>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val boardService: BoardService
    ) : BoardRepository {
        override fun uploadWriting( token: String, title: String, content: String , uuid: String ): Either<Failure, UploadResult> {
            return when(networkHandler.isNetworkAvailable()){
                true -> networkHandler.request(
                    boardService.uploadRequest(token, title, content, uuid),
                    { it.toUploadResult() },
                    UploadResultEntity.empty
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
    }
}