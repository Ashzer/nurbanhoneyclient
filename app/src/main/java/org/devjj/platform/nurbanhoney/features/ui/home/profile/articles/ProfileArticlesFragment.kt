package org.devjj.platform.nurbanhoney.features.ui.home.profile.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentProfileArticlesBinding
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileArticle
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import javax.inject.Inject

@AndroidEntryPoint
class ProfileArticlesFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_profile_articles

    @Inject
    lateinit var navigator: Navigator

    private var _binding: FragmentProfileArticlesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileArticlesViewModel>()

    @Inject
    lateinit var articlesAdapter: ProfileArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            observe(articles, ::renderArticles)
            observe(boards, ::responseGetBoards)
        }
    }

    private fun responseGetBoards(boards: List<Board>?) {
        viewModel.getArticles()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.prefsNurbanTokenKey = getString(R.string.prefs_nurban_token_key)
        viewModel.prefsUserIdKey = getString(R.string.prefs_user_id)

        binding.profileArticlesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.profileArticlesRv.adapter = articlesAdapter

        viewModel.getBoards()


        articlesAdapter.clickListener = { id, boardName ->
            var board = viewModel.getBoard(boardName)
            navigator.showArticle(requireActivity(),board,id)
        }

    }

    private fun renderArticles(articles: List<ProfileArticle>?) {
        articlesAdapter.collection = articles.orEmpty()
        articles?.forEach {
            Log.d("profile_check__", it.toString())
        }
    }
}