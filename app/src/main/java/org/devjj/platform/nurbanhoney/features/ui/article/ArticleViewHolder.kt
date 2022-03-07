package org.devjj.platform.nurbanhoney.features.ui.article

import android.view.View
import android.widget.ImageView
import androidx.core.view.size
import androidx.gridlayout.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.databinding.ArticleHeaderBinding
import org.devjj.platform.nurbanhoney.features.ui.article.model.Article
import org.devjj.platform.nurbanhoney.features.ui.article.model.Ratings

class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var binding = ArticleHeaderBinding.bind(itemView)
    fun bind(
        article: Article,
        ratings: Ratings,
        likeArticleClickListener: (View) -> Unit,
        dislikeArticleClickListener: (View) -> Unit
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
            // background = ContextCompat.getDrawable(itemView.context, R.drawable.edges_rectangle)
            //setPadding(5, 5, 5, 5)
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