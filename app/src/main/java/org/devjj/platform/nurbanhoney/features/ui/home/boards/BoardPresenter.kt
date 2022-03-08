package org.devjj.platform.nurbanhoney.features.ui.home.boards

import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem

interface BoardPresenter {
    fun initializeArticles()
    fun requestMoreArticles()
    fun renderMoreArticles(articles : List<ArticleItem>?)
    fun removePresenter()
}