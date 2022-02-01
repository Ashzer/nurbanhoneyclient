package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

import android.os.Bundle
import android.util.Log
import android.view.View
import org.devjj.platform.nurbanhoney.core.extension.invisible

class BoardPopularFragment : BoardFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardWriteFab.invisible()

    }

    override fun boardArticleClickListener() : (Int,String?) -> Unit = { id,address ->
        var board = viewModel.getBoard(address?:"")
        Log.d("popular_check__", board.toString())
        navigator.showArticle(requireActivity(),board, id)
    }
}