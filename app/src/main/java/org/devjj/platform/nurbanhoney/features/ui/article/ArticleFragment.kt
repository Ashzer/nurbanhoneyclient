package org.devjj.platform.nurbanhoney.features.ui.article

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
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
        private const val PARAM_BOARD = "param_board"

        fun forArticle(board: String?, id: Int?) =
            ArticleFragment().apply {
                arguments = bundleOf(PARAM_BOARD to board, PARAM_ARTICLE to id)
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
        binding.articleBody.articleLikesTv.text = ratings?.likes.toString()
        binding.articleBody.articleDislikesTv.text = ratings?.dislikes.toString()
        Log.d("rating_check__", ratings?.myRating.toString() ?: "empty")
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
        binding.articleBody.articleContentWv.html = article?.content.toString()
        binding.articleHeader.articleTitleTv.text = article?.title
        binding.articleHeader.articleInquiriesTv.text = article?.inquiries.toString()

        binding.articleHeader.articleInfoNicknameTv.text = article?.nickname
        binding.articleHeader.articleInfoBadgeIv.loadFromUrl(
            article?.badge.toString(),
            R.drawable.ic_action_no_badge
        )

        binding.articleBody.articleLikesIv.loadFromDrawable(R.drawable.ic_action_like) //.loadFromUrl(article?.badge.toString(), R.drawable.ic_action_no_badge)
        binding.articleBody.articleLikesTv.text = article?.likes.toString()
        binding.articleBody.articleDislikesIv.loadFromDrawable(R.drawable.ic_action_dislike)
        binding.articleBody.articleDislikesTv.text = article?.dislikes.toString()
        binding.articleBody.articleShareIv.loadFromDrawable(R.drawable.ic_action_share)

        viewModel.getComments()
        if (!viewModel.isAuthor()) {
            binding.articleHeader.articleInfoModifyClo.invisible()
            binding.articleHeader.articleInfoDeleteClo.invisible()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArticleId(arguments?.get(PARAM_ARTICLE) as Int)

        binding.articleBody.articleContentWv.setInputEnabled(false)

        binding.articleHeader.articleInfoModifyClo.setOnSingleClickListener {
            getConfirmation(requireContext(), "글을 수정하시겠습니까?") {
                //viewModel.deleteArticle()
                CoroutineScope(Dispatchers.IO).async {
                    navigator.showTextEditorToModifyWithLoginCheck(
                        requireContext(),
                        viewModel.board,
                        viewModel.article.value!!
                    )
                }
                //navigator.showTextEditorToModify(requireContext(),viewModel.article.value!!)
            }
        }

        binding.articleHeader.articleInfoDeleteClo.setOnSingleClickListener {
            getConfirmation(requireContext(), "글을 삭제하시겠습니까?") {
                viewModel.deleteArticle()
            }
        }

        binding.articleBody.articleLikesClo.setOnSingleClickListener {
            Log.d("rating_check__+", viewModel.ratings.value?.myRating.toString() ?: "empty")
            if (viewModel.ratings.value?.myRating.toString() == "like") {
                viewModel.unLike()
            } else {
                viewModel.postLike()
            }
        }

        binding.articleBody.articleDislikesClo.setOnSingleClickListener {
            Log.d("rating_check__+", viewModel.ratings.value?.myRating.toString() ?: "empty")
            if (viewModel.ratings.value?.myRating.toString() == "dislike") {
                viewModel.unDislike()
            } else {
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

        binding.articleTail.articleCommentBtn.setOnSingleClickListener {
            getConfirmation(requireContext(), "댓글을 등록하시겠습니까?") {
                viewModel.postComment(binding.articleTail.articleCommentEt.text.toString())
                binding.articleTail.articleCommentEt.text.clear()
            }
        }

        binding.articleBody.articleCommentsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.articleBody.articleCommentsRv.adapter = commentAdapter

        binding.articleTail.articleCommentVisibilityClo.setOnSingleClickListener {
            Log.d("equals_check__", binding.articleTail.articleCommentVisibilityTv.text.toString())
            Log.d("equals_check__", resources.getString(R.string.comment_drop_up))
            if (binding.articleTail.articleCommentVisibilityTv.text.toString()
                    .equals(resources.getString(R.string.comment_drop_up))
            ) {
                binding.articleTail.articleCommentVisibilityIv.loadFromDrawable(R.drawable.ic_action_dropdown)
                binding.articleTail.articleCommentVisibilityTv.setText(R.string.comment_drop_down)
                binding.articleTail.articleCommentClo.visible()
            } else {
                binding.articleTail.articleCommentVisibilityIv.loadFromDrawable(R.drawable.ic_action_dropup)
                binding.articleTail.articleCommentVisibilityTv.setText(R.string.comment_drop_up)
                binding.articleTail.articleCommentClo.invisible()
            }
        }

        commentAdapter.deleteClickListener = { id ->
            getConfirmation(requireContext(), "댓글을 삭제하시겠습니까?") { viewModel.deleteComment(id) }
        }

        commentAdapter.modifyClickListener = {
            showKeyboard(requireActivity(), it)
            binding.articleTail.articleCommentClo.invisible()
            binding.articleTail.articleCommentVisibilityClo.invisible()
        }

        commentAdapter.updateClickListener = { view, comment, id ->
            getConfirmation(requireContext(), "댓글을 수정하시겠습니까?") {
                viewModel.updateComment(comment, id)
                binding.articleTail.articleCommentClo.visible()
                binding.articleTail.articleCommentVisibilityClo.visible()
                removeKeyboard(requireActivity(), view)
            }
        }

        commentAdapter.cancelClickListener = {
            binding.articleTail.articleCommentClo.visible()
            binding.articleTail.articleCommentVisibilityClo.visible()
            removeKeyboard(requireActivity(), it)
        }

        // viewModel.controller.init()
        // viewModel.controller.getNext(viewModel.comments.value)
        //  viewModel.controller.loadNext()
        val scrollBounds = Rect()
        binding.articleContainerSv.getHitRect(scrollBounds)

        binding.articleContainerSv.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.d("scroll_check__", " $scrollX , $scrollY , $oldScrollX , $oldScrollY ")
            if (!v.canScrollVertically(1)) {
                viewModel.getNextComments()
            }

//            Log.d("recyclerview_check__" , (binding.articleCommentsRv.layoutManager as LinearLayoutManager).itemCount.toString())
//            Log.d("recyclerview_check__" , (binding.articleCommentsRv.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition().toString())
        }
    }

    override fun onResume() {
        viewModel.setArticleId(arguments?.get(PARAM_ARTICLE) as Int)
        super.onResume()
    }
}