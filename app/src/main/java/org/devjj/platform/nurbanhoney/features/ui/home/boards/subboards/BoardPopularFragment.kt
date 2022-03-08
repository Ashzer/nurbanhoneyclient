package org.devjj.platform.nurbanhoney.features.ui.home.boards.subboards

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.devjj.platform.nurbanhoney.core.extension.invisible
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.home.boards.BoardFragment

class BoardPopularFragment : BoardFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNewArticleForbidden()
    }

    override fun boardArticleClickListener() : (Int, Board) -> Unit = { id, board ->
        Log.d("popular_check__", board.toString())
        navigator.showArticle(requireActivity(),board, id)
    }
}