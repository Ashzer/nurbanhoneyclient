package org.devjj.platform.nurbanhoney.features.network.repositories.article.usecases

import org.devjj.platform.nurbanhoney.core.interactor.UseCase
import org.devjj.platform.nurbanhoney.features.network.repositories.article.ArticleRepository
import org.devjj.platform.nurbanhoney.features.ui.article.Article
import javax.inject.Inject

class GetArticleUseCase
@Inject constructor(
    private val repository: ArticleRepository
) : UseCase<Article, GetArticleUseCase.Params>(){

    override suspend fun run(params: Params) = repository.getArticle(params.token,params.id)
    data class Params(val token : String, val id : Int)
}