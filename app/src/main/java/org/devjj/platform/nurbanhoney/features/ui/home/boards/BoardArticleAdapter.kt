package org.devjj.platform.nurbanhoney.features.ui.home.boards

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_article.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import javax.inject.Inject

class BoardArticleAdapter
@Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var collection: MutableList<ArticleItem> = mutableListOf()

    fun addAll(adder: List<ArticleItem>) {
        val oldSize = collection.size
        collection.addAll(adder)
        notifyItemRangeInserted(oldSize, adder.size)
    }

    fun clear() {
        collection = mutableListOf()
    }

    internal var clickListener: (Int, Board) -> Unit = { _, _ -> }

    override fun getItemCount() = collection.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_article))

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        (viewHolder as ViewHolder).bind(collection[position], clickListener)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(articleItem: ArticleItem, clickListener: (Int, Board) -> Unit) {

            if (articleItem.thumbnail != "" && articleItem.thumbnail != "null") {
                itemView.itemArticleThumbnailIv.loadFromUrl(
                    articleItem.thumbnail,
                    R.drawable.ic_action_no_thumbnail
                )
            } else {
                itemView.itemArticleThumbnailIv.loadFromDrawable(
                    R.drawable.ic_action_no_thumbnail
                )
            }

            with(itemView){
                itemArticleTitleTv.text = articleItem.title
                itemArticleRepliesTv.text = " [${articleItem.replies}]"
                itemArticleBadgeIv.loadFromUrl(
                    articleItem.badge,
                    R.drawable.ic_action_no_badge
                )
                itemArticleUserNameTv.text = articleItem.author
                itemArticleBoardTv.visible()
                itemArticleBoardTv.text = articleItem.board.name
                setOnSingleClickListener {
                    clickListener(articleItem.id, articleItem.board)
                }
            }
        }
    }
}