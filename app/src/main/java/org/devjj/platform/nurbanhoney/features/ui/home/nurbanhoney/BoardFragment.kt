package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

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
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.extension.setOnSingleClickListener
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentNurbanboardBinding
import org.devjj.platform.nurbanhoney.features.ui.home.BoardActivity
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import javax.inject.Inject

@AndroidEntryPoint
open class BoardFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_nurbanboard

    @Inject
    lateinit var navigator: Navigator

    private var _binding: FragmentNurbanboardBinding? = null
    protected val binding get() = _binding!!
    val viewModel by viewModels<BoardViewModel>()

    @Inject
    lateinit var articleAdapter: BoardArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            observe(articles, ::renderArticles)
            failure(failure, ::failureHandler)
        }

    }

    private fun renderArticles(articles: List<NurbanHoneyArticle>?) {
        articleAdapter.collection = articles.orEmpty()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var bundle = this.arguments
        if (bundle != null) {
            viewModel.board = bundle.getParcelable(R.string.BoardInfo.toString()) ?: Board.empty
            Log.d("bundle_check__",viewModel.board.toString())
        }

        _binding = FragmentNurbanboardBinding.inflate(inflater, container, false)
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

        //viewModel.controller.initialize()

        binding.boardListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

//                val position =
//                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//                val count = (recyclerView.adapter?.itemCount ?: 0)
//
//                if(count - 1 == position){
                if (!binding.boardListRv.canScrollVertically(1)) {
                    viewModel.getArticles()
                }


            }
        })
        articleAdapter.clickListener = boardArticleClickListener()

    }

    protected open fun boardArticleClickListener() : (Int,String?) -> Unit = { id,_ ->
        navigator.showArticle(requireActivity(),viewModel.board, id)
    }

    override fun onResume() {
        super.onResume()
        viewModel.controller.initialize()

    }

}