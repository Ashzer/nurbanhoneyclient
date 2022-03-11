package org.devjj.platform.nurbanhoney.features.network.repositories.board.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.UnitTest
import org.devjj.platform.nurbanhoney.core.functional.Either.Right
import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.network.repositories.board.BoardRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GetBoardsUseCaseTest : UnitTest() {
    private lateinit var getBoards: GetBoardsUseCase

    var repository = mockk<BoardRepository>()

    @BeforeEach
    fun setUp() {
        getBoards = GetBoardsUseCase(repository)
        every { repository.getBoards() } returns Right(
            listOf(
                Board(0, "너반꿀", "nurban"),
                Board(1, "자유", "free")
            )
        )
    }

    @Test
    @DisplayName("게시판 목록 가져오기")
    fun `get board list from repository`(){
        runBlocking { getBoards.run(UseCase.None()) }

        verify(exactly = 1) { repository.getBoards() }
    }
}