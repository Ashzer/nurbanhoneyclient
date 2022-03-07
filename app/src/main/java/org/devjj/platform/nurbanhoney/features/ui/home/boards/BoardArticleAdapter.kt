package org.devjj.platform.nurbanhoney.features.ui.home.boards

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_article.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.ui.home.boards.model.ArticleItem
import javax.inject.Inject
import kotlin.properties.Delegates

class BoardArticleAdapter
@Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var collection: MutableList<ArticleItem> = mutableListOf()

    fun insertFront(adder : List<ArticleItem>){
        var oldSize = collection.size
        collection.addAll(adder)
        notifyItemRangeInserted(oldSize,adder.size)
    }

    internal var clickListener: (Int, Board) -> Unit = { _, _ -> }

    override fun getItemCount() = collection.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_article))

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        (viewHolder as ViewHolder).bind(collection[position], clickListener)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(articleItem: ArticleItem, clickListener: (Int, Board) -> Unit) {

            //Log.d("thumbnail_check__", "??? ${articleItem.thumbnail}")
            if (articleItem.thumbnail != "null" && articleItem.thumbnail != null && articleItem.thumbnail != "") {
                itemView.itemArticleThumbnailIv.loadFromUrl(
                    articleItem.thumbnail,
                    R.drawable.ic_action_no_thumbnail
                )
            } else {
                itemView.itemArticleThumbnailIv.loadFromDrawable(
                    R.drawable.ic_action_no_thumbnail
                )
            }
            itemView.itemArticleTitleTv.text = articleItem.title
            itemView.itemArticleRepliesTv.text = " [${articleItem.replies}]"
            itemView.itemArticleBadgeIv.loadFromUrl(
                articleItem.badge,
                R.drawable.ic_action_no_badge
            )

            Log.d("badge_check__", "??? ${articleItem.badge}")
            itemView.itemArticleUserNameTv.text = articleItem.author

            //게시판 이름 표시
//            if(nurbanHoneyArticle.board.isNullOrBlank()) {
//                itemView.itemArticleBoardTv.invisible()
//            }else{
                itemView.itemArticleBoardTv.visible()
                itemView.itemArticleBoardTv.text = articleItem.board.name

            //}

            itemView.setOnSingleClickListener {
                clickListener(articleItem.id, articleItem.board)
            }
        }
    }
}