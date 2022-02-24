package org.devjj.platform.nurbanhoney.features.ui.home.profile.comments

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_profile_comment.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inflate
import org.devjj.platform.nurbanhoney.core.extension.setOnSingleClickListener
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileComment
import org.devjj.platform.nurbanhoney.features.Board
import javax.inject.Inject
import kotlin.properties.Delegates

class ProfileCommentsAdapter
@Inject constructor() : RecyclerView.Adapter<ProfileCommentsAdapter.ViewHolder>() {
    internal var collection: List<ProfileComment> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var clickListener: (Int, Board) -> Unit = { _, _ -> }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ViewHolder(parent.inflate(R.layout.item_profile_comment))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position],clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: ProfileComment, clickListener: (Int, Board) -> Unit){
            itemView.itemProfileCommentTv.text = comment.content
            itemView.itemProfileBoardTv.text= comment.board.name
            itemView.itemProfileTitleTv.text = comment.title
            itemView.itemProfileDateTv.text=
                "${comment.createAt.split("T")[0]} ${comment.createAt.split("T")[1].substring(0,8)}"

            itemView.setOnSingleClickListener {
                clickListener(comment.articleId,comment.board)
            }
        }
    }
}