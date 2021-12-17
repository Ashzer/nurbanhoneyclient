package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

import android.os.Bundle
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
import javax.inject.Inject

@AndroidEntryPoint
class NurbanHoneyFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_nurbanboard

    @Inject
    lateinit var navigator: Navigator

    private var _binding: FragmentNurbanboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NurbanHoneyViewModel>()

    @Inject
    lateinit var articleAdapter: NurbanArticleAdapter

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
        _binding = FragmentNurbanboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardWriteFab.setOnSingleClickListener {
            //navigator.showTextEditor(requireContext())
            CoroutineScope(Dispatchers.IO).async {
                navigator.showTextEditorWithLoginCheck(requireContext())
            }
        }

        articleAdapter.clickListener = { id ->
            navigator.showArticle(requireActivity(), viewModel.board, id)
        }

        val set: MutableSet<NurbanHoneyArticle> = mutableSetOf()
        set.add(NurbanHoneyArticle(0, "", "", 0, "", "", ""))

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.controller.initialize()
    }

}