package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_article.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import javax.inject.Inject
import kotlin.properties.Delegates

class BoardArticleAdapter
@Inject constructor() : RecyclerView.Adapter<BoardArticleAdapter.ViewHolder>() {

    internal var collection: List<NurbanHoneyArticle> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var clickListener: (Int, Board) -> Unit = { _, _ -> }

    override fun getItemCount() = collection.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_article))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], clickListener)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(nurbanHoneyArticle: NurbanHoneyArticle, clickListener: (Int, Board) -> Unit) {

            Log.d("thumbnail_check__", "??? ${nurbanHoneyArticle.thumbnail}")
            if (nurbanHoneyArticle.thumbnail != "null" && nurbanHoneyArticle.thumbnail != null && nurbanHoneyArticle.thumbnail != "") {
                itemView.itemArticleThumbnailIv.loadFromUrl(
                    nurbanHoneyArticle.thumbnail,
                    R.drawable.ic_action_no_thumbnail
                )
            } else {
                itemView.itemArticleThumbnailIv.loadFromDrawable(
                    R.drawable.ic_action_no_thumbnail
                )
            }
            itemView.itemArticleTitleTv.text = nurbanHoneyArticle.title
            itemView.itemArticleRepliesTv.text = " [${nurbanHoneyArticle.replies}]"
            itemView.itemArticleBadgeIv.loadFromUrl(
                nurbanHoneyArticle.badge,
                R.drawable.ic_action_no_badge
            )

            Log.d("badge_check__", "??? ${nurbanHoneyArticle.badge}")
            itemView.itemArticleUserNameTv.text = nurbanHoneyArticle.author

            //게시판 이름 표시
//            if(nurbanHoneyArticle.board.isNullOrBlank()) {
//                itemView.itemArticleBoardTv.invisible()
//            }else{
                itemView.itemArticleBoardTv.visible()
                itemView.itemArticleBoardTv.text = nurbanHoneyArticle.board.name

            //}

            itemView.setOnSingleClickListener {
                clickListener(nurbanHoneyArticle.id, nurbanHoneyArticle.board)
            }
        }
    }
}