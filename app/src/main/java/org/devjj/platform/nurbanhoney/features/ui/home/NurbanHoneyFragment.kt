package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.extension.failure
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentNurbanboardBinding
import org.devjj.platform.nurbanhoney.features.network.BoardService
import org.devjj.platform.nurbanhoney.features.ui.textedit.TextEditorRepository
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
    @Inject
    lateinit var prefs: SharedPreferences
    @Inject
    lateinit var textEditorRepository: TextEditorRepository

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

    private fun failureHandler(failure: Failure?){
        Log.d("failure_check__", failure?.toString() ?: "failure null")
        when (failure) {
            is Failure.NetworkConnection -> {
                Log.d("Fragment_failure",R.string.failure_network_connection.toString())
            }
            is Failure.ServerError -> {
                Log.d("Fragment_failure", R.string.failure_server_error.toString())
            }
            is Failure.TokenError ->{
                Failure.TokenError(this.requireContext())
            }
            else -> {
                Log.d("Fragment_failure", R.string.failure_else_error.toString())
            }
        }
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

        binding.WriteNurban.setOnClickListener {
            navigator.showTextEditor(requireActivity())
        }

        binding.rvNurbanBoard.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNurbanBoard.adapter = articleAdapter

        viewModel.getArticles(
            prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
            flag = 0,
            offset = 0,
            limit = 10
        )
    }
}