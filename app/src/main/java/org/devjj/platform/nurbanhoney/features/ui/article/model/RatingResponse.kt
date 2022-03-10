package org.devjj.platform.nurbanhoney.features.ui.article.model

import org.devjj.platform.nurbanhoney.core.extension.empty

data class RatingResponse(val result: String){
    companion object{
        val empty = RatingResponse(String.empty())
    }
}