package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

import android.os.Bundle
import android.util.Log
import android.view.View
import org.devjj.platform.nurbanhoney.core.extension.invisible
import org.devjj.platform.nurbanhoney.features.ui.splash.Board

class BoardPopularFragment : BoardFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardWriteFab.invisible()

    }

    override fun boardArticleClickListener() : (Int, Board) -> Unit = { id, board ->

        Log.d("popular_check__", board.toString())
        navigator.showArticle(requireActivity(),board, id)
    }
}