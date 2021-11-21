package org.devjj.platform.nurbanhoney.features.ui.article

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.extension.failure
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrl
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentArticleBinding
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : BaseFragment() {

    companion object {
        private const val PARAM_ARTICLE = "param_article"

        fun forArticle(id: Int?) =
            ArticleFragment().apply {
                arguments = bundleOf(PARAM_ARTICLE to id)
            }
    }

    override fun layoutId() = R.layout.fragment_article

    private val viewModel by viewModels<ArticleViewModel>()
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var commentAdapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            observe(article, ::renderArticle)
            observe(likes, ::renderLikes)
            observe(commentResponse, ::responseComments)
            observe(comments, ::responseComments)
            failure(failure, ::failureHandler)
        }
    }

    private fun responseComments(result : String?){
        Log.d("comment_check__",result)
        viewModel.initComments()
    }
    private fun responseComments(comment : List<Comment>?){
        comment?.forEach {
            Log.d("comment_check__",it.toString())
        }
        commentAdapter.collection = comment.orEmpty()
    }

    private fun renderLikes(likes : Pair<Int,Int>?){
        binding.articleLikesTv.text = likes?.first.toString()
        binding.articleDislikesTv.text = likes?.second.toString()
    }

    private fun renderArticle(article: Article?) {
        binding.articleContentWv.html = article?.content.toString()
        binding.articleTitleTv.text = article?.title
        binding.articleInquiriesTv.text = article?.inquiries.toString()

        binding.articleInfoNicknameTv.text = article?.nickname
        binding.articleInfoBadgeIv.loadFromUrl(
            article?.badge.toString(),
            R.drawable.ic_action_no_badge
        )

        binding.articleLikesIv.loadFromUrl(article?.badge.toString(), R.drawable.ic_action_no_badge)
        binding.articleLikesTv.text = article?.likes.toString()
        binding.articleDislikesIv.loadFromUrl(
            article?.badge.toString(),
            R.drawable.ic_action_no_badge
        )
        binding.articleDislikesTv.text = article?.dislikes.toString()
        binding.articleShareIv.loadFromUrl(article?.badge.toString(), R.drawable.ic_action_no_badge)

        viewModel.getComments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getArticle(arguments?.get(PARAM_ARTICLE) as Int)

        binding.articleContentWv.setInputEnabled(false)
        binding.articleLikesClo.setOnClickListener {
            viewModel.postLike()
        }

        binding.articleDislikesClo.setOnClickListener {
            viewModel.postDislike()
        }
        /*
        binding.articleCommentEt.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }*/

        binding.articleCommentBtn.setOnClickListener {
            viewModel.postComment( binding.articleCommentEt.text.toString() )
            binding.articleCommentEt.text.clear()
        }

        binding.articleCommentsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.articleCommentsRv.adapter = commentAdapter

        commentAdapter.deleteClickListener = { id ->

            AlertDialog.Builder(this.requireContext())
                .setMessage("댓글을 삭제하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    viewModel.deleteComment(id)
                }
                .setNegativeButton("취소") { _,_ ->
                    Toast.makeText(this.requireContext(),"취소 되었습니다", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        commentAdapter.insertClickListener = {

        }
    }



}