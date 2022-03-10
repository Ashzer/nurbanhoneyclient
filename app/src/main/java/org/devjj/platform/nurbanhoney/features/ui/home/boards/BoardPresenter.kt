package org.devjj.platform.nurbanhoney.features.ui.home.boards

import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.BoardViewModel

interface BoardPresenter {
    fun initializeArticles(adapter: BoardArticleAdapter, viewModel: BoardViewModel)
    fun requestMoreArticles(viewModel: BoardViewModel)
    fun renderMoreArticles(
        adapter: BoardArticleAdapter,
        viewModel: BoardViewModel,
        articles: List<ArticleItem>?
    )

}