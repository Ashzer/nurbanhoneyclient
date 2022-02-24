package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import javax.inject.Inject

class GetArticlesUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<List<ArticleItem>, GetArticlesUseCase.Params>() {
    override suspend fun run(params: Params) =
        repository.getArticles(params.board, params.flag, params.offset, params.limit)

    data class Params(val board: String, val flag: Int, val offset: Int, val limit: Int)
}