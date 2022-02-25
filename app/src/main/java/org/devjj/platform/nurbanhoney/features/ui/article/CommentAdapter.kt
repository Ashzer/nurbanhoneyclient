package org.devjj.platform.nurbanhoney.features.ui.article

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.gridlayout.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.core.imageprocessing.ImageViewHandler
import org.devjj.platform.nurbanhoney.databinding.ArticleHeaderBinding
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.ui.article.model.ArticleViewType
import org.devjj.platform.nurbanhoney.features.ui.article.model.Comment
import org.devjj.platform.nurbanhoney.features.ui.article.model.Ratings
import javax.inject.Inject
import kotlin.properties.Delegates

class CommentAdapter
@Inject constructor(
    val imageViewHandler: ImageViewHandler
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var userId by Delegates.notNull<Int>()
    private var header = Article.empty
    var collection: MutableList<Comment> = mutableListOf()
    private var ratings = Ratings.empty

    fun setAdapterHeader(article: Article, ratings: Ratings) {
        header = article
        this.ratings = ratings
        notifyItemChanged(0)
    }

    fun insertTail(comments: List<Comment>) {
        var oldSize = collection.size
        collection.addAll(comments)
        notifyItemRangeInserted(oldSize, comments.size)
        //notifyDataSetChanged()
    }

    fun updateRatings(ratings: Ratings) {
        this.ratings = ratings
        notifyItemChanged(0)
    }


    internal var deleteClickListener: (Int) -> Unit = { _ -> }
    internal var updateClickListener: (View, String, Int) -> Unit = { _, _, _ -> }
    internal var modifyClickListener: (View) -> Unit = { _ -> }
    internal var cancelClickListener: (View) -> Unit = { _ -> }

    internal var likeArticleClickListener: (View) -> Unit = {}
    internal var dislikeArticleClickListener: (View) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ArticleViewType.COMMENT -> {
                return CommentViewHolder(parent.inflate(R.layout.item_comment))
            }
            ArticleViewType.ARTICLE -> {
                return ArticleViewHolder(parent.inflate(R.layout.article_header))
            }
        }
        throw Error("Shouldn't get here")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CommentViewHolder -> {
                holder.bind(
                    collection[position - 1],
                    deleteClickListener,
                    updateClickListener,
                    modifyClickListener,
                    cancelClickListener,
                    userId,
                )
            }
            is ArticleViewHolder -> {
                holder.bind(
                    header,
                    ratings,
                    likeArticleClickListener,
                    dislikeArticleClickListener,
                    imageViewHandler
                )
            }
        }
    }

    override fun getItemCount() = collection.size + 1

    override fun getItemViewType(position: Int) =
        if (position == 0) ArticleViewType.ARTICLE else ArticleViewType.COMMENT


    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ArticleHeaderBinding.bind(itemView)
        fun bind(
            article: Article,
            ratings: Ratings,
            likeArticleClickListener: (View) -> Unit,
            dislikeArticleClickListener: (View) -> Unit,
            imageViewHandler: ImageViewHandler
        ) {
            binding.articleContentWv.html = article.content
            binding.articleTitleTv.text = article.title
            binding.articleInquiriesTv.text = article.inquiries.toString()

            binding.articleInfoNicknameTv.text = article.nickname
            binding.articleInfoBadgeIv.loadFromUrl(
                article.badge,
                R.drawable.ic_action_no_badge
            )

            binding.articleLikesIv.loadFromDrawable(R.drawable.ic_action_like) //.loadFromUrl(article?.badge.toString(), R.drawable.ic_action_no_badge)
            binding.articleLikesTv.text = article.likes.toString()
            binding.articleDislikesIv.loadFromDrawable(R.drawable.ic_action_dislike)
            binding.articleDislikesTv.text = article.dislikes.toString()
            binding.articleShareIv.loadFromDrawable(R.drawable.ic_action_share)

            binding.articleContentWv.setInputEnabled(false)

            if (binding.articleInfoInsigniaLlo.size == 0) {
                article.insignia.forEach {
                    addInsigniaImage(it)
                }
            }


            likeArticleClickListener(binding.articleLikesClo)
            dislikeArticleClickListener(binding.articleDislikesClo)

            setLikesDislikes(ratings)
            if (hasMyRating(ratings)) {
                if (ratings.myRating == "like") {
                    likeSelected()
                } else {
                    dislikeSelected()
                }
            } else {
                nothingSelected()
            }

        }

        // 휘장 이미지 셋팅하는 메소드
        private fun addInsigniaImage(url: String) {
            val iv = ImageView(itemView.context)
            with(iv) {
                loadFromUrl(url, R.drawable.ic_action_no_badge)
                setMargins(iv, 0, 0, 5, 0)
                background = ContextCompat.getDrawable(itemView.context, R.drawable.edges_rectangle)
                setPadding(5, 5, 5, 5)
                binding.articleInfoInsigniaLlo.addView(this)
            }
        }

        private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
            var gl = GridLayout.LayoutParams()
            gl.setMargins(left, top, right, bottom)
            view.layoutParams = gl
        }


        private fun setLikesDislikes(ratings: Ratings?) {
            binding.articleLikesTv.text = ratings?.likes.toString()
            binding.articleDislikesTv.text = ratings?.dislikes.toString()
        }

        private fun hasMyRating(ratings: Ratings?) = !ratings?.myRating.isNullOrEmpty()

        private fun likeSelected() {
            binding.articleLikesIv.setColor(R.color.white)
            binding.articleLikesIv.setBackgroundDrawable(R.drawable.likes_border)
            binding.articleLikesIv.setBackgroundColorId(R.color.colorAccent)
            binding.articleDislikesIv.setColor(R.color.colorAccent)
            binding.articleDislikesIv.background = null
        }

        private fun dislikeSelected() {
            binding.articleLikesIv.setColor(R.color.colorAccent)
            binding.articleLikesIv.background = null
            binding.articleDislikesIv.setColor(R.color.white)
            binding.articleDislikesIv.setBackgroundDrawable(R.drawable.likes_border)
            binding.articleDislikesIv.setBackgroundColorId(R.color.colorAccent)
        }

        private fun nothingSelected() {
            binding.articleLikesIv.setColor(R.color.colorAccent)
            binding.articleLikesIv.background = null
            binding.articleDislikesIv.setColor(R.color.colorAccent)
            binding.articleDislikesIv.background = null
        }
    }
}