package org.devjj.platform.nurbanhoney.features.ui.home.profile.articles

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_profile_article.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inflate
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrl
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileArticle
import javax.inject.Inject
import kotlin.properties.Delegates

class ProfileArticlesAdapter
@Inject constructor() : RecyclerView.Adapter<ProfileArticlesAdapter.ViewHolder>() {

    internal var collection: List<ProfileArticle> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_profile_article))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position])

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: ProfileArticle) {
            Log.d("profile_article_check__", article.toString())
            itemView.itemProfileArticleThumbnailIv.loadFromUrl(
                article.thumbnail,
                R.drawable.ic_action_no_thumbnail
            )

            itemView.itemProfileArticleTitleTv.text = article.title
            itemView.itemProfileArticleCommentsTv.text = "[${article.commentCount}]"
            itemView.itemProfileArticleBoardTv.text = article.flag
            itemView.itemProfileArticleDateTv.text =
                "${article.createAt.split("T")[0]} ${article.createAt.split("T")[1].substring(0,8)}"

        }
    }
}