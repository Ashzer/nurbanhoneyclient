package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
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
    lateinit var boardService: BoardService

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var textEditorRepository: TextEditorRepository

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

        val articles = mutableListOf(
            NurbanHoneyArticle(
                "https://nurbanboard.s3.ap-northeast-2.amazonaws.com/default.png",
                "testElement",
                3,
                "https://nurbanhoneyprofile.s3.ap-northeast-2.amazonaws.com/default.png",
                "nickname",
                ""
            )
        )

        val adapter = NurbanArticleAdapter(articles)
        binding.rvNurbanBoard.adapter = adapter
        binding.rvNurbanBoard.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getArticles(
            prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
            offset = 0,
            limit = 10
        )

//        CoroutineScope(Dispatchers.IO).async {
//            Log.d("getArts_check__", "before")
//            textEditorRepository.getArticles( prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
//                offset = 0,
//                limit = 10).apply {
//            }
//        }
    }
}