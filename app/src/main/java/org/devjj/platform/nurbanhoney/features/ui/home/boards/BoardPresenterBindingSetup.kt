package org.devjj.platform.nurbanhoney.features.ui.home.boards

import org.devjj.platform.nurbanhoney.databinding.FragmentBoardBinding
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.BoardViewModel

interface BoardBindingPresenter : BoardPresenter {
    fun setup(
        binding: FragmentBoardBinding,
        adapter: BoardArticleAdapter,
        viewModel: BoardViewModel
    )
}