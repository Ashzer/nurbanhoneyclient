package org.devjj.platform.nurbanhoney.features.ui.article

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.controller.removeKeyboard
import org.devjj.platform.nurbanhoney.core.controller.showKeyboard
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentArticleBinding
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : BaseFragment() {
    companion object {
        private const val PARAM_ARTICLE = "param_article"
        private const val PARAM_BOARD = "param_board"

        fun forArticle(board: Board?, id: Int?) =
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
    ): View {
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

    override fun onResume() {
        viewModel.setArticleId(arguments?.get(PARAM_ARTICLE) as Int)
        super.onResume()
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
        Log.d("rating_check__", ratings?.myRating.toString())
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
        viewModel.board = (arguments?.get(PARAM_BOARD) as Board)

        binding.articleBody.articleContentWv.setInputEnabled(false)
        binding.articleBody.articleCommentsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.articleBody.articleCommentsRv.adapter = commentAdapter

        //글 수정 버튼
        articleModifyBtnListener(binding.articleHeader.articleInfoModifyClo)
        //글 삭제 버튼
        articleDeleteBtnListener(binding.articleHeader.articleInfoDeleteClo)
        //좋아요 버튼
        likePressedListener(binding.articleBody.articleLikesClo)
        //싫어요 버튼
        dislikePressedListener(binding.articleBody.articleDislikesClo)
        //댓글 영역 보이기/숨김
        commentAreaVisibilityListener(binding.articleTail.articleCommentVisibilityClo)
        //댓글 등록 버튼
        commentUpdateBtnListener(binding.articleTail.articleCommentBtn)
        //댓글 수정 영역 보이기
        commentAdapter.modifyClickListener = commentModifyAreaSetVisibleListener()
        //댓글 수정 영역 숨기기(수정 취소)
        commentAdapter.cancelClickListener = commentModifyAreaSetInvisibleListener()
        //댓글 수정 버튼(수정 완료)
        commentAdapter.updateClickListener = commentModifyBtnListener()
        //댓글 삭제 버튼
        commentAdapter.deleteClickListener = commentDeleteBtnListener()
        //댓글 추가 로드
        loadCommentsOnScrollViewListener(binding.articleContainerSv)
    }

    private fun commentModifyAreaSetVisibleListener(): (View) -> Unit = {
        showKeyboard(requireActivity(), it)
        binding.articleTail.articleCommentClo.invisible()
        binding.articleTail.articleCommentVisibilityClo.invisible()
    }

    private fun commentModifyAreaSetInvisibleListener(): (View) -> Unit = {
        binding.articleTail.articleCommentClo.visible()
        binding.articleTail.articleCommentVisibilityClo.visible()
        removeKeyboard(requireActivity(), it)
    }

    private fun commentModifyBtnListener(): (View, String, Int) -> Unit = { view, comment, id ->
        getConfirmation(requireContext(), "댓글을 수정하시겠습니까?") {
            viewModel.updateComment(comment, id)
            binding.articleTail.articleCommentClo.visible()
            binding.articleTail.articleCommentVisibilityClo.visible()
            removeKeyboard(requireActivity(), view)
        }
    }

    private fun commentDeleteBtnListener(): (Int) -> Unit = { id ->
        getConfirmation(requireContext(), "댓글을 삭제하시겠습니까?") { viewModel.deleteComment(id) }
    }

    private fun articleDeleteBtnListener(view: View) = view.setOnSingleClickListener {
        getConfirmation(requireContext(), "글을 삭제하시겠습니까?") {
            viewModel.deleteArticle()
        }
    }

    private fun articleModifyBtnListener(view: View) = view.setOnSingleClickListener {
        getConfirmation(requireContext(), "글을 수정하시겠습니까?") {
            CoroutineScope(Dispatchers.IO).launch {
                navigator.showTextEditorToModifyWithLoginCheck(
                    requireContext(),
                    viewModel.board,
                    viewModel.article.value!!
                )
            }
            //navigator.showTextEditorToModify(requireContext(),viewModel.article.value!!)
        }
    }

    private fun likePressedListener(view: View) = view.setOnSingleClickListener {
        Log.d("rating_check__+", viewModel.ratings.value?.myRating.toString())
        if (viewModel.ratings.value?.myRating.toString() == "like") {
            viewModel.unLike()
        } else {
            viewModel.postLike()
        }
    }

    private fun dislikePressedListener(view: View) = view.setOnSingleClickListener {
        Log.d("rating_check__+", viewModel.ratings.value?.myRating.toString())
        if (viewModel.ratings.value?.myRating.toString() == "dislike") {
            viewModel.unDislike()
        } else {
            viewModel.postDislike()
        }
    }

    private fun commentAreaVisibilityListener(view: View) = view.setOnSingleClickListener {
        Log.d("equals_check__", binding.articleTail.articleCommentVisibilityTv.text.toString())
        Log.d("equals_check__", resources.getString(R.string.comment_drop_up))
        if (binding.articleTail.articleCommentVisibilityTv.text.toString() == resources.getString(R.string.comment_drop_up)
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

    private fun commentUpdateBtnListener(view: View) = view.setOnSingleClickListener {
        getConfirmation(requireContext(), "댓글을 등록하시겠습니까?") {
            viewModel.postComment(binding.articleTail.articleCommentEt.text.toString())
            binding.articleTail.articleCommentEt.text.clear()
        }
    }

    private fun loadCommentsOnScrollViewListener(view: View) =
        view.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            view.getHitRect(Rect())
            Log.d("scroll_check__", " $scrollX , $scrollY , $oldScrollX , $oldScrollY ")
            if (!v.canScrollVertically(1)) {
                viewModel.getNextComments()
            }
        }
}