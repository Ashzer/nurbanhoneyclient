package org.devjj.platform.nurbanhoney.features.ui.article

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inflate
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.ui.article.model.ArticleViewType
import org.devjj.platform.nurbanhoney.features.ui.article.model.Comment
import org.devjj.platform.nurbanhoney.features.ui.article.model.Ratings
import javax.inject.Inject
import kotlin.properties.Delegates

class ArticleDetailAdapter
@Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                    cancelClickListener
                )
            }
            is ArticleViewHolder -> {
                holder.bind(
                    header,
                    ratings,
                    likeArticleClickListener,
                    dislikeArticleClickListener
                )
            }
        }
    }

    override fun getItemCount() = collection.size + 1

    override fun getItemViewType(position: Int) =
        if (position == 0) ArticleViewType.ARTICLE else ArticleViewType.COMMENT

}