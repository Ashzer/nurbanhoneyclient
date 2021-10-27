package org.devjj.platform.nurbanhoney.features.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_article.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrl

class NurbanArticleAdapter(
    var articles: List<NurbanHoneyArticle>
) : RecyclerView.Adapter<NurbanArticleAdapter.NurbanArticleViewHolder>() {
    inner class NurbanArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount() = articles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NurbanArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return NurbanArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: NurbanArticleViewHolder, position: Int) {
        holder.itemView.apply {
            ivThumbnail.loadFromUrl(articles[position].thumbnail)
            tvTitle.text = articles[position].title
            tvReplies.text = "[ ${articles[position].replies} ]"
            ivBadge.loadFromUrl(articles[position].badge)
            tvUserName.text = articles[position].author
        }
    }
}