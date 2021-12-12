package org.devjj.platform.nurbanhoney.features.ui.article

import android.app.AlertDialog
import android.graphics.drawable.Drawable
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.controller.removeKeyboard
import org.devjj.platform.nurbanhoney.core.controller.showKeyboard
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
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
    lateinit var navigator: Navigator

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
            observe(comments, ::initComments)
            failure(failure, ::failureHandler)
        }
    }

    private fun setArticleId(id: Int?) {
        viewModel.getArticle()
        viewModel.getRatings()
    }

    private fun responseRating(result: String?) {
        Log.d("rating_check__", result.toString())
        viewModel.getRatings()
    }

    private fun responseComments(result: String?) {
        //viewModel.initComments()
        Log.d("Controller_check__", "comment initialized")
        viewModel.controller.initialize()
    }

    private fun responseComment(result: String?) {
        viewModel.getComment(viewModel.updatingCommentId)
    }

    private fun initComments(comment: List<Comment>?) {
        commentAdapter.collection = comment.orEmpty()
    }

    private fun renderLikes(ratings: Ratings?) {
        binding.articleLikesTv.text = ratings?.likes.toString()
        binding.articleDislikesTv.text = ratings?.dislikes.toString()
        Log.d("rating_check__",ratings?.myRating.toString() ?: "empty")
        if (!ratings?.myRating.isNullOrEmpty()) {
            /*if (ratings?.myRating == "like") {
                binding.articleLikesIv.drawable.setTint(R.color.colorWhite)
                binding.articleLikesIv.setColorFilter(R.color.colorWhite)
            } else {
                binding.articleDislikesIv.drawable.setTint(R.color.colorWhite)
                binding.articleDislikesIv.setColorFilter(R.color.colorWhite)
            }*/
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
        if(!viewModel.isAuthor()){
            binding.articleInfoModifyClo.invisible()
            binding.articleInfoDeleteClo.invisible()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArticleId(arguments?.get(PARAM_ARTICLE) as Int)

        binding.articleContentWv.setInputEnabled(false)

        binding.articleInfoModifyClo.setOnSingleClickListener {
            getConfirmation(requireContext(),"글을 수정하시겠습니까?") {
                //viewModel.deleteArticle()
                CoroutineScope(Dispatchers.IO).async {
                    navigator.showTextEditorToModifyWithLoginCheck(
                        requireContext(),
                        viewModel.article.value!!
                    )
                }
                //navigator.showTextEditorToModify(requireContext(),viewModel.article.value!!)
            }
        }

        binding.articleInfoDeleteClo.setOnSingleClickListener {
            getConfirmation(requireContext(),"글을 삭제하시겠습니까?") {
                viewModel.deleteArticle()
            }
        }

        binding.articleLikesClo.setOnSingleClickListener {
            Log.d("rating_check__+",viewModel.ratings.value?.myRating.toString() ?: "empty")
            if(viewModel.ratings.value?.myRating.toString() == "like"){
                viewModel.unLike()
            }else{
                viewModel.postLike()
            }
        }

        binding.articleDislikesClo.setOnSingleClickListener {
            Log.d("rating_check__+",viewModel.ratings.value?.myRating.toString() ?: "empty")
            if(viewModel.ratings.value?.myRating.toString() == "dislike"){
                viewModel.unDislike()
            }else{
                viewModel.postDislike()
            }
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
            getConfirmation(requireContext(),"댓글을 등록하시겠습니까?") {
                viewModel.postComment(binding.articleCommentEt.text.toString())
                binding.articleCommentEt.text.clear()
            }
        }

        binding.articleCommentsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.articleCommentsRv.adapter = commentAdapter

        binding.articleCommentVisibilityClo.setOnSingleClickListener {
            Log.d("equals_check__", binding.articleCommentVisibilityTv.text.toString())
            Log.d("equals_check__", resources.getString(R.string.comment_drop_up))
            if (binding.articleCommentVisibilityTv.text.toString().equals(resources.getString(R.string.comment_drop_up))) {
                binding.articleCommentVisibilityIv.loadFromDrawable(R.drawable.ic_action_dropdown)
                binding.articleCommentVisibilityTv.setText(R.string.comment_drop_down)
                binding.articleCommentClo.visible()
            } else {
                binding.articleCommentVisibilityIv.loadFromDrawable(R.drawable.ic_action_dropup)
                binding.articleCommentVisibilityTv.setText(R.string.comment_drop_up)
                binding.articleCommentClo.invisible()
            }
        }

        commentAdapter.deleteClickListener = { id ->
            getConfirmation(requireContext(),"댓글을 삭제하시겠습니까?") { viewModel.deleteComment(id) }
        }

        commentAdapter.modifyClickListener = {
            showKeyboard(requireActivity(), it)
            binding.articleCommentClo.invisible()
            binding.articleCommentVisibilityClo.invisible()
        }

        commentAdapter.updateClickListener = { view, comment, id ->
            getConfirmation(requireContext(),"댓글을 수정하시겠습니까?") {
                viewModel.updateComment(comment, id)
                binding.articleCommentClo.visible()
                binding.articleCommentVisibilityClo.visible()
                removeKeyboard(requireActivity(), view)
            }
        }

        commentAdapter.cancelClickListener = {
            binding.articleCommentClo.visible()
            binding.articleCommentVisibilityClo.visible()
            removeKeyboard(requireActivity(), it)
        }

       // viewModel.controller.init()
       // viewModel.controller.getNext(viewModel.comments.value)
      //  viewModel.controller.loadNext()

        binding.articleContainerSv.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.d("scroll_check__"," $scrollX , $scrollY , $oldScrollX , $oldScrollY ")
            if(!v.canScrollVertically(1)){
                viewModel.getNextComments()
            }

        }
    }

    override fun onResume() {
        viewModel.setArticleId(arguments?.get(PARAM_ARTICLE) as Int)
        super.onResume()
    }
/*
    private fun getConfirmation(msg: String, action: () -> Unit) {
        AlertDialog.Builder(this.requireContext())
            .setMessage(msg)
            .setPositiveButton("확인") { _, _ ->
                action()
            }
            .setNegativeButton("취소") { _, _ ->
                Toast.makeText(this.requireContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
*/
}