package org.devjj.platform.nurbanhoney.features.ui.home.boards

import org.devjj.platform.nurbanhoney.core.extension.isIterable
import org.devjj.platform.nurbanhoney.databinding.FragmentBoardBinding
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.BoardViewModel
import javax.inject.Inject

class BoardBindingPresenterImpl
@Inject constructor() : BoardBindingPresenter {


    override fun setup(
        binding: FragmentBoardBinding,
        adapter: BoardArticleAdapter,
        viewModel: BoardViewModel
    ) {
    }

    override fun initializeArticles(adapter: BoardArticleAdapter, viewModel: BoardViewModel) {
        adapter.clear()
        viewModel.clear()
        viewModel.initArticles()
    }

    override fun renderMoreArticles(
        adapter: BoardArticleAdapter,
        viewModel: BoardViewModel,
        articles: List<ArticleItem>?
    ) {
        if (articles.isIterable()) {
            val adder = getListAdapterNotContains(articles!!, adapter.collection)
            addNewItemsToAdapterOrRequestMoreArticles(adapter, viewModel, adder)
        }
    }

    private fun getListAdapterNotContains(add: List<ArticleItem>, dest: List<ArticleItem>) =
        add.filterNot { dest.contains(it) }.toList()

    private fun addNewItemsToAdapterOrRequestMoreArticles(
        adapter: BoardArticleAdapter,
        viewModel: BoardViewModel,
        adder: List<ArticleItem>
    ) {
        if (adder.isIterable()) {
            adapter.addAll(adder)
        } else {
            requestMoreArticles(viewModel)
        }
    }

    override fun requestMoreArticles(viewModel: BoardViewModel) = viewModel.getArticles()

}