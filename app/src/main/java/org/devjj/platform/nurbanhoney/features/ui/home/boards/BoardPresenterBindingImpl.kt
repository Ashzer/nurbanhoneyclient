package org.devjj.platform.nurbanhoney.features.ui.home.boards

import org.devjj.platform.nurbanhoney.core.extension.isIterable
import org.devjj.platform.nurbanhoney.databinding.FragmentBoardBinding
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.BoardViewModel
import javax.inject.Inject

class BoardPresenterBindingImpl
@Inject constructor() : BoardPresenterBinding {
    var binding: FragmentBoardBinding? = null
    var adapter: BoardArticleAdapter? = null
    var viewModel: BoardViewModel? = null

    override fun setup(
        binding: FragmentBoardBinding,
        adapter: BoardArticleAdapter,
        viewModel: BoardViewModel
    ) {
        this.binding = binding
        this.adapter = adapter
        this.viewModel = viewModel
    }

    override fun initializeArticles() {
        adapter?.clear()
        viewModel?.clear()
        viewModel?.initArticles()
    }

    override fun renderMoreArticles(articles: List<ArticleItem>?) {
        if (articles.isIterable()) {
            val adder = getListAdapterNotContains(articles!!, adapter!!.collection)
            addNewItemsToAdapterOrRequestMoreArticles(adder)
        }
    }

    private fun getListAdapterNotContains(add: List<ArticleItem>, dest: List<ArticleItem>) =
        add.filterNot { dest.contains(it) }.toList()

    private fun addNewItemsToAdapterOrRequestMoreArticles(adder: List<ArticleItem>) {
        if (adder.isIterable()) {
            adapter?.addAll(adder)
        } else {
            requestMoreArticles()
        }
    }

    override fun requestMoreArticles() = viewModel!!.getArticles()

    override fun removePresenter() {
        binding = null
        adapter = null
        viewModel = null
    }
}