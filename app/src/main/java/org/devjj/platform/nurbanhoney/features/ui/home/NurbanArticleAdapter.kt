package org.devjj.platform.nurbanhoney.features.ui.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_article.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inflate
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrl
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrlThumbnail
import javax.inject.Inject
import kotlin.properties.Delegates

class NurbanArticleAdapter
@Inject constructor() : RecyclerView.Adapter<NurbanArticleAdapter.ViewHolder>() {

    internal var collection: List<NurbanHoneyArticle> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount() = collection.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_article))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(nurbanHoneyArticle: NurbanHoneyArticle) {
            itemView.ivThumbnail.loadFromUrlThumbnail(nurbanHoneyArticle.thumbnail)
            itemView.tvTitle.text = nurbanHoneyArticle.title
            itemView.tvReplies.text = "[ ${nurbanHoneyArticle.replies} ]"
            itemView.ivBadge.loadFromUrl(nurbanHoneyArticle.badge)

            itemView.tvUserName.text = nurbanHoneyArticle.author
        }
    }
}