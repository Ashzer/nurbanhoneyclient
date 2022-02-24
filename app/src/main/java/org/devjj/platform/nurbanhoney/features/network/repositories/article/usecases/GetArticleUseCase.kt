package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import javax.inject.Inject

class GetArticleUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<Article, GetArticleUseCase.Params>() {

    override suspend fun run(params: Params) =
        repository.getArticle(params.board, params.token, params.id)

    data class Params(val board: String, val token: String, val id: Int)
}