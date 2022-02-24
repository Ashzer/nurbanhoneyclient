package org.devjj.platform.nurbanhoney.features.network.repositories.board.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.board.BoardRepository
import org.devjj.platform.nurbanhoney.features.Board
import javax.inject.Inject

class GetBoardsUseCase
@Inject constructor(
    private val repository: BoardRepository
) : UseCase<List<Board>, UseCase.None>() {
    override suspend fun run(params: None) = repository.getBoards()
}