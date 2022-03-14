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
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentBoardBinding
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.home.HomeActivity
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.BoardViewModel
import javax.inject.Inject

@AndroidEntryPoint
open class BoardFragment : BaseFragment() {


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
            observe(newArticles, ::newArticleResponse)
            failure(failure, ::failureHandler)
        }
        val bundle = this.arguments
        viewModel.board = bundle?.getParcelable(R.string.BoardInfo.toString()) ?: Board.empty
    }

    override fun onDestroyView() {
        binding.boardListRv.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun newArticleResponse(articleItems: List<ArticleItem>?) {
        if (articleItems.isIterable()) {
            val adder = getListAdapterNotContains(articleItems!!, articleAdapter.collection)
            addNewItemsToAdapterOrRequestMoreArticles(articleAdapter, viewModel, adder)
        }
    }

    private fun getListAdapterNotContains(add: List<ArticleItem>, dest: List<ArticleItem>) =
        add.filterNot { dest.contains(it) }.toList()

    private fun addNewItemsToAdapterOrRequestMoreArticles(
        adapter: BoardArticleAdapter,
        viewModel: BoardViewModel,
        adder: List<ArticleItem>
    ) {
        if (adder.isIterable()) {
            adapter.addAll(adder)
        } else {
            viewModel.getArticles()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as HomeActivity).setActionBarTitle(viewModel.board.name)

        binding.boardListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.boardListRv.adapter = articleAdapter

        viewModel.initArticles()

        setNewArticleWritable()
        setRecyclerViewLoadMoreListener()

        articleAdapter.clickListener = boardArticleClickListener()

    }

    private fun setNewArticleWritable() {
        binding.boardWriteFab.visible()
        binding.boardWriteFab.setOnSingleClickListener {
            runBlocking {
                navigator.showTextEditorWithLoginCheck(requireContext(), viewModel.board)
            }
        }
    }

    protected fun setNewArticleForbidden() {
        binding.boardWriteFab.invisible()
        binding.boardWriteFab.setOnSingleClickListener { }
    }

    private fun setRecyclerViewLoadMoreListener() {
        var oldCount = 0
        binding.boardListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.getLinearLayoutManager()
                val position = layoutManager.findLastVisibleItemPosition()
                val count = (recyclerView.adapter?.itemCount ?: 0)

                if (crossedThresholdFirstTime(count, position, oldCount)) {
                    viewModel.getArticles()
                    oldCount = count
                }
            }
        })
    }

    private fun crossedThresholdFirstTime(count: Int, position: Int, oldCount: Int) =
        crossedThreshold(count, position) && isNewCondition(oldCount, count)

    private fun crossedThreshold(count: Int, position: Int): Boolean {
        val threshold = 10
        return count < (position + threshold)
    }

    private fun isNewCondition(oldCount: Int, count: Int): Boolean = oldCount != count

    protected open fun boardArticleClickListener(): (Int, Board) -> Unit = { id, _ ->
        navigator.showArticle(requireActivity(), viewModel.board, id)
        Log.d("bundle_check__", viewModel.board.toString())
    }
}