package org.devjj.platform.nurbanhoney.features.ui.article

import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.ui.article.model.Comment
import org.devjj.platform.nurbanhoney.features.ui.article.model.Ratings
import org.devjj.platform.nurbanhoney.features.Board
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

    private val viewModel by viewModels<ArticleViewModel>()
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var commentAdapter: CommentAdapter

    @Inject
    lateinit var prefs : SharedPreferences

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

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().finish()
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
        setLikesDislikes(ratings)
        if (hasMyRating(ratings)) {
            if (ratings?.myRating == "like") {
                likeSelected()
            } else {
                dislikeSelected()
            }
        }else{
            nothingSelected()
        }
    }

    private fun setLikesDislikes(ratings: Ratings?){
        binding.articleHeader.articleLikesTv.text = ratings?.likes.toString()
        binding.articleHeader.articleDislikesTv.text = ratings?.dislikes.toString()
    }

    private fun hasMyRating(ratings: Ratings?) = !ratings?.myRating.isNullOrEmpty()

    private fun likeSelected(){
        binding.articleHeader.articleLikesIv.setColor(R.color.white)
        binding.articleHeader.articleLikesIv.setBackgroundDrawable(R.drawable.likes_border)
        binding.articleHeader.articleLikesIv.setBackgroundColorId(R.color.colorAccent)
        binding.articleHeader.articleDislikesIv.setColor(R.color.colorAccent)
        binding.articleHeader.articleDislikesIv.background = null
    }

    private fun dislikeSelected(){
        binding.articleHeader.articleLikesIv.setColor(R.color.colorAccent)
        binding.articleHeader.articleLikesIv.background =null
        binding.articleHeader.articleDislikesIv.setColor(R.color.white)
        binding.articleHeader.articleDislikesIv.setBackgroundDrawable(R.drawable.likes_border)
        binding.articleHeader.articleDislikesIv.setBackgroundColorId(R.color.colorAccent)
    }

    private fun nothingSelected(){
        binding.articleHeader.articleLikesIv.setColor(R.color.colorAccent)
        binding.articleHeader.articleLikesIv.background =null
        binding.articleHeader.articleDislikesIv.setColor(R.color.colorAccent)
        binding.articleHeader.articleDislikesIv.background = null
    }

    private fun renderArticle(article: Article?) {
        binding.articleHeader.articleContentWv.html = article?.content.toString()
        binding.articleHeader.articleTitleTv.text = article?.title
        binding.articleHeader.articleInquiriesTv.text = article?.inquiries.toString()

        binding.articleHeader.articleInfoNicknameTv.text = article?.nickname
        binding.articleHeader.articleInfoBadgeIv.loadFromUrl(
            article?.badge.toString(),
            R.drawable.ic_action_no_badge
        )

        binding.articleHeader.articleLikesIv.loadFromDrawable(R.drawable.ic_action_like) //.loadFromUrl(article?.badge.toString(), R.drawable.ic_action_no_badge)
        binding.articleHeader.articleLikesTv.text = article?.likes.toString()
        binding.articleHeader.articleDislikesIv.loadFromDrawable(R.drawable.ic_action_dislike)
        binding.articleHeader.articleDislikesTv.text = article?.dislikes.toString()
        binding.articleHeader.articleShareIv.loadFromDrawable(R.drawable.ic_action_share)

        viewModel.getComments()
        if (!viewModel.isAuthor()) {
            binding.articleHeader.articleInfoModifyClo.invisible()
            binding.articleHeader.articleInfoDeleteClo.invisible()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.prefsNurbanTokenKey = getString(R.string.prefs_nurban_token_key)
        viewModel.prefsUserIdKey = getString(R.string.prefs_user_id)

        viewModel.setArticleId(arguments?.get(PARAM_ARTICLE) as Int)
        viewModel.board = (arguments?.get(PARAM_BOARD) as Board)

        binding.articleHeader.articleContentWv.setInputEnabled(false)
        binding.articleBody.articleCommentsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.articleBody.articleCommentsRv.adapter = commentAdapter

        commentAdapter.userId = prefs.getString(getString(R.string.prefs_user_id),"-1")?.toInt() ?: -1



        //글 수정 버튼
        articleModifyBtnListener(binding.articleHeader.articleInfoModifyClo)
        //글 삭제 버튼
        articleDeleteBtnListener(binding.articleHeader.articleInfoDeleteClo)
        //좋아요 버튼
        likePressedListener(binding.articleHeader.articleLikesClo)
        //싫어요 버튼
        dislikePressedListener(binding.articleHeader.articleDislikesClo)
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
            //Log.d("scroll_check__", " $scrollX , $scrollY , $oldScrollX , $oldScrollY ")
            if (!v.canScrollVertically(1)) {
                viewModel.getNextComments()
            }
            //Log.d("rvVisible_check__",((binding.articleContainerSv.articleCommentsRv.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition().toString())
        }
}