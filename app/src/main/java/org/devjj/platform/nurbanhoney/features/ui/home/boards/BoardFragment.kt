package org.devjj.platform.nurbanhoney.features.ui.home.boards

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.failure
import org.devjj.platform.nurbanhoney.core.extension.getLinearLayoutManager
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.extension.setOnSingleClickListener
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentBoardBinding
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.home.BoardActivity
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.BoardViewModel
import javax.inject.Inject

@AndroidEntryPoint
open class BoardFragment : BaseFragment() {
    //override fun layoutId() = R.layout.fragment_nurbanboard

    @Inject
    lateinit var navigator: Navigator

    private var _binding: FragmentBoardBinding? = null
    protected val binding get() = _binding!!
    val viewModel by viewModels<BoardViewModel>()

    @Inject
    lateinit var articleAdapter: BoardArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            observe(newArticles, ::renderArticles)
            failure(failure, ::failureHandler)
        }

    }

    override fun onDestroyView() {
        binding.boardListRv.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun renderArticles(articleItems: List<ArticleItem>?) {
        //articleAdapter.collection = articleItems.orEmpty()
        if (isIterable(articleItems)) {
            var adder = articleItems!!.filter { !articleAdapter.collection.contains(it) }.toList()
            if (adder.isNotEmpty()) {
                articleAdapter.insertFront(adder)
            } else {
                viewModel.getArticles()
                Log.d("recycler_scroll_check__", "이게 왜?")
            }
        }
    }

    private fun isIterable(collection: Collection<Any>?) = !collection.isNullOrEmpty()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var bundle = this.arguments
        if (bundle != null) {
            viewModel.board = bundle.getParcelable(R.string.BoardInfo.toString()) ?: Board.empty
            Log.d("bundle_check__", viewModel.board.toString())
        }

        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.prefsNurbanTokenKey = getString(R.string.prefs_nurban_token_key)
        viewModel.prefsUserIdKey = getString(R.string.prefs_user_id)

        viewModel.getBoards()

        (requireActivity() as BoardActivity).setActionBarTitle(viewModel.board.name)
        binding.boardWriteFab.setOnSingleClickListener {
            //navigator.showTextEditor(requireContext())
            CoroutineScope(Dispatchers.IO).async {
                navigator.showTextEditorWithLoginCheck(requireContext(), viewModel.board)
            }
        }


        binding.boardListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.boardListRv.adapter = articleAdapter

        viewModel.initArticles()
        var oldCount = 0
        binding.boardListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                //(recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val layoutManager = recyclerView.getLinearLayoutManager()
                val position = layoutManager.findLastVisibleItemPosition()
                val count = (recyclerView.adapter?.itemCount ?: 0)

                if ((count < position + 10) && oldCount != count) {
                    Log.d("scroll_check__", binding.boardListRv.canScrollVertically(1).toString())
                    viewModel.getArticles()
                    oldCount = count
                }
            }
        })
        articleAdapter.clickListener = boardArticleClickListener()

    }

    protected open fun boardArticleClickListener(): (Int, Board) -> Unit = { id, _ ->
        navigator.showArticle(requireActivity(), viewModel.board, id)
    }
}