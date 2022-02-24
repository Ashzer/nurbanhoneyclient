package org.devjj.platform.nurbanhoney.features.ui.home.profile.comments

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
import org.devjj.platform.nurbanhoney.databinding.FragmentProfileCommentsBinding
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileComment
import javax.inject.Inject

@AndroidEntryPoint
class ProfileCommentsFragment : BaseFragment() {
    //override fun layoutId() = R.layout.fragment_profile_comments

    private var _binding: FragmentProfileCommentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileCommentsViewModel>()

    @Inject
    lateinit var commentsAdapter: ProfileCommentsAdapter
    @Inject
    lateinit var navigator : Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel){
            observe(comments, ::renderComments)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.prefsNurbanTokenKey = getString(R.string.prefs_nurban_token_key)
        viewModel.prefsUserIdKey = getString(R.string.prefs_user_id)

        binding.profileCommentsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.profileCommentsRv.adapter = commentsAdapter
        commentsAdapter.clickListener = { id, board ->
            navigator.showArticle(requireActivity(),board,id)
        }
        viewModel.getComments()
    }

    private fun renderComments(comments : List<ProfileComment>?){
        commentsAdapter.collection = comments.orEmpty()

        comments?.forEach {
            Log.d("profile_check__", it.toString())
        }
    }
}