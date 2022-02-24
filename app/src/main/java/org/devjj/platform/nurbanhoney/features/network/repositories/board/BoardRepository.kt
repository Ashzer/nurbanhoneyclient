package org.devjj.platform.nurbanhoney.features.network.repositories.board

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.platform.NetworkHandler
import org.devjj.platform.nurbanhoney.features.Board
import javax.inject.Inject

interface BoardRepository {
    fun getBoards(): Either<Failure, List<Board>>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val boardService: BoardService
    ) :BoardRepository{
        override fun getBoards(): Either<Failure, List<Board>> {
            return when(networkHandler.isNetworkAvailable()){
                true-> networkHandler.request(
                    boardService.getBoards(),
                    {it.map{BoardEntity -> BoardEntity.toBoard()}},
                    emptyList()
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
    }
}