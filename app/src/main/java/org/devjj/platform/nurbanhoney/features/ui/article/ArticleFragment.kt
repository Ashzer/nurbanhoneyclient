package org.devjj.platform.nurbanhoney.features.ui.article

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.ui.article.model.Comment
import org.devjj.platform.nurbanhoney.features.ui.article.model.Ratings
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : BaseFragment(){
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
    lateinit var prefs: SharedPreferences

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
            observe(newComments, ::initComments)
            failure(failure, ::failureHandler)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.article_toolbar_menu, menu)
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
        viewModel.initComments()
    }

    private fun responseComment(result: String?) {
        //   viewModel.getComment(viewModel.updatingCommentId)
    }

    var oldCount = 0
    private fun initComments(comments: List<Comment>?) {
        if (!comments.isNullOrEmpty()) {
            var adder = comments.filter { !commentAdapter.collection.contains(it) }
            if (adder.isNotEmpty()) {
                commentAdapter.insertTail(comments)
            } else {
                viewModel.getComments()
                Log.d("comment_check", "이게 왜?")
            }
        }
        //Log.d("comment_check__", commentAdapter.collection.toString())


        binding.articleCommentsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.getLinearLayoutManager()
                val position = layoutManager.findLastVisibleItemPosition()
                val count = recyclerView.adapter?.itemCount ?: 0
                val threshold = 10
                if ((count < position + threshold) && oldCount != count) {
                    viewModel.getComments()
                    oldCount = count
                }
            }
        })
    }

    private fun renderLikes(ratings: Ratings?) {
        if (ratings != null) {
            commentAdapter.updateRatings(ratings)
        }
    }

    private fun renderArticle(article: Article?) {
        if (article != null)
            commentAdapter.setAdapterHeader(
                article,
                Ratings(article.likes, article.dislikes, article.myRating)
            )


        viewModel.getComments()
        if (!viewModel.isAuthor()) {
            (requireActivity() as ArticleActivity).hideActionMenu()
//            binding.articleHeader.articleInfoModifyClo.invisible()
//            binding.articleHeader.articleInfoDeleteClo.invisible()
        } else {
            (requireActivity() as ArticleActivity).showActionMenu()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.prefsNurbanTokenKey = getString(R.string.prefs_nurban_token_key)
        viewModel.prefsUserIdKey = getString(R.string.prefs_user_id)

        viewModel.setArticleId(arguments?.get(PARAM_ARTICLE) as Int)
        viewModel.board = (arguments?.get(PARAM_BOARD) as Board)

        binding.articleCommentsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.articleCommentsRv.adapter = commentAdapter

        commentAdapter.userId =
            prefs.getString(getString(R.string.prefs_user_id), "-1")?.toInt() ?: -1


        //글 수정 버튼
        (requireActivity() as ArticleActivity).modifyArticleClickListener =
            articleModifyBtnListener()
        //글 삭제 버튼
        (requireActivity() as ArticleActivity).deleteArticleClickListener =
            articleDeleteBtnListener()
        //좋아요 버튼
        commentAdapter.likeArticleClickListener = likePressedListener()
        //싫어요 버튼
        commentAdapter.dislikeArticleClickListener = dislikePressedListener()


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
        //loadCommentsOnScrollViewListener(binding.articleContainerSv)
        //댓글 줄수 제한
        var commentMaxLines = resources.getInteger(R.integer.comment_max_lines)
        setCommentsLimitLines(binding.articleTail.articleCommentEt, commentMaxLines)
        binding.articleTail.articleCommentEt.hint = getString(
            R.string.comment_max_lines_hint,
            resources.getInteger(R.integer.comment_max_length),
            commentMaxLines
        )

        binding.articleCommentFab.setOnSingleClickListener {
            var commentDialog = CommentDialog()
            commentDialog.show(requireActivity().supportFragmentManager, "comment_dialog")

            commentDialog.uploadComment(object  : CommentDialog.CommentDialogUploadCallback{
                override fun uploadComment(comment: String) {
                    viewModel.postComment(comment)
                }
            })
        }

    }

    private fun setCommentsLimitLines(editText: EditText, maxLines: Int) =
        editText.addTextChangedListener(object : TextWatcher {
            var previousString = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                previousString = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.articleTail.articleCommentEt.lineCount > maxLines) {
                    binding.articleTail.articleCommentEt.setText(previousString)
                    binding.articleTail.articleCommentEt.setSelection(binding.articleTail.articleCommentEt.length())
                }
            }
        })

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

    private fun articleDeleteBtnListener(): () -> Unit = {
        getConfirmation(requireContext(), "글을 삭제하시겠습니까?") {
            viewModel.deleteArticle()
        }
    }

    private fun articleModifyBtnListener(): () -> Unit = {
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

    private fun likePressedListener(): (View) -> Unit = {
        it.setOnSingleClickListener {
            Log.d("rating_check__+", viewModel.ratings.value?.myRating.toString())
            if (viewModel.ratings.value?.myRating.toString() == "like") {
                viewModel.unLike()
            } else {
                viewModel.postLike()
            }
        }
    }

    private fun dislikePressedListener(): (View) -> Unit = {
        it.setOnSingleClickListener {
            Log.d("rating_check__+", viewModel.ratings.value?.myRating.toString())
            if (viewModel.ratings.value?.myRating.toString() == "dislike") {
                viewModel.unDislike()
            } else {
                viewModel.postDislike()
            }
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


}