package org.devjj.platform.nurbanhoney.features.ui.article

import android.app.AlertDialog
import android.graphics.BlendMode
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
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
            observe(articleId, ::setArticleId)
            observe(article, ::renderArticle)
            observe(ratingResponse, ::responseRating)
            observe(ratings, ::renderLikes)
            observe(commentResponse, ::responseComment)
            observe(commentsResponse, ::responseComments)
            observe(comments, ::responseComments)
            failure(failure, ::failureHandler)
        }
    }

    private fun setArticleId( id: Int? ){
        viewModel.getArticle()
    }

    private fun responseRating(result : String?){
        viewModel.getRatings()
    }
    private fun responseComments(result: String?) {
        viewModel.initComments()
    }

    private fun responseComment(result: String?){
        viewModel.getComment(viewModel.updatingCommentId)
    }

    private fun responseComments(comment: List<Comment>?) {
        commentAdapter.collection = comment.orEmpty()
    }

    private fun renderLikes(ratings: Ratings?) {
        binding.articleLikesTv.text = ratings?.likes.toString()
        binding.articleDislikesTv.text = ratings?.dislikes.toString()
        if(!ratings?.myRating.isNullOrEmpty()){
            if(ratings?.myRating == "like"){
                binding.articleLikesIv.drawable.setTint(R.color.colorWhite)
                binding.articleLikesIv.setColorFilter(R.color.colorWhite)
            }else{
                binding.articleDislikesIv.drawable.setTint(R.color.colorWhite)
                binding.articleDislikesIv.setColorFilter(R.color.colorWhite)
            }
        }
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

        binding.articleLikesIv.loadFromDrawable(R.drawable.ic_action_like) //.loadFromUrl(article?.badge.toString(), R.drawable.ic_action_no_badge)
        binding.articleLikesTv.text = article?.likes.toString()
        binding.articleDislikesIv.loadFromDrawable(R.drawable.ic_action_dislike)
        binding.articleDislikesTv.text = article?.dislikes.toString()
        binding.articleShareIv.loadFromDrawable(R.drawable.ic_action_share)

        viewModel.getComments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArticleId(arguments?.get(PARAM_ARTICLE) as Int)

        binding.articleContentWv.setInputEnabled(false)
        binding.articleLikesClo.setOnSingleClickListener {
            viewModel.postLike()
        }

//        binding.articleDislikesClo.setOnClickListener {
//            viewModel.postDislike()
//        }
        binding.articleDislikesClo.setOnSingleClickListener{
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

        binding.articleCommentBtn.setOnSingleClickListener {
            AlertDialog.Builder(this.requireContext())
                .setMessage("댓글을 등록하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    viewModel.postComment(binding.articleCommentEt.text.toString())
                    binding.articleCommentEt.text.clear()
                }
                .setNegativeButton("취소") { _, _ ->
                    Toast.makeText(this.requireContext(), "취소되었습니다", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        binding.articleCommentsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.articleCommentsRv.adapter = commentAdapter

        commentAdapter.deleteClickListener = { id ->
            AlertDialog.Builder(this.requireContext())
                .setMessage("댓글을 삭제하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    viewModel.deleteComment(id)
                }
                .setNegativeButton("취소") { _, _ ->
                    Toast.makeText(this.requireContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        commentAdapter.insertClickListener = {

        }

        commentAdapter.updateClickListener ={ comment, id ->
            viewModel.updateComment(comment, id)
        }
    }

}