package org.devjj.platform.nurbanhoney.features.ui.article

import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comment.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import javax.inject.Inject
import kotlin.properties.Delegates

class CommentAdapter
@Inject constructor(
    private val prefs: SharedPreferences,
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    internal var collection: List<Comment> by Delegates.observable(emptyList()) { _, _, _ ->
        //notifyItemInserted(old.size)
        notifyDataSetChanged()
    }

    internal var deleteClickListener: (Int) -> Unit = { _ -> }
    internal var updateClickListener: (View, String, Int) -> Unit = { _, _, _ -> }
    internal var modifyClickListener: (View) -> Unit = { _ -> }
    internal var cancelClickListener: (View) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_comment))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(
            collection[position],
            deleteClickListener,
            updateClickListener,
            modifyClickListener,
            cancelClickListener,
            prefs
        )

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            comment: Comment,
            deleteClickListener: (Int) -> Unit,
            updateClickListener: (View, String, Int) -> Unit,
            modifyClickListener: (View) -> Unit,
            cancelClickListener: (View) -> Unit,
            prefs: SharedPreferences
        ) {

            if (prefs.getString(R.string.prefs_user_id.toString(), "-1")
                    ?.toInt() ?: -1 != comment.userId
            ) {
                itemView.itemCommentModifyBtnClo.invisible()
                itemView.itemCommentDeleteBtnClo.invisible()
            }

            itemView.itemCommentBadgeIv.loadFromUrl(comment.badge, R.drawable.ic_action_no_badge)
            itemView.itemCommentNicknameTv.text = comment.nickname
            itemView.itemCommentContentTv.text = comment.comment
            itemView.itemCommentDeleteBtnClo.setOnSingleClickListener {
                deleteClickListener(comment.id)
            }

            itemView.itemCommentModifyBtnClo.setOnSingleClickListener {
                itemView.itemCommentDeleteBtnClo.invisible()
                itemView.itemCommentModifyBtnClo.invisible()
                itemView.itemCommentUpdateBtnClo.visible()
                itemView.itemCommentCancelBtnClo.visible()
                itemView.itemCommentUpdateEtClo.visible()
                itemView.itemCommentUpdateEt.setText(itemView.itemCommentContentTv.text)
                itemView.itemCommentContentClo.invisible()
                itemView.itemCommentUpdateEt.isFocusable = true
                itemView.itemCommentUpdateEt.requestFocus()
                modifyClickListener(itemView.itemCommentUpdateEt)
            }

            itemView.itemCommentUpdateBtnClo.setOnSingleClickListener {
                itemView.itemCommentDeleteBtnClo.visible()
                itemView.itemCommentModifyBtnClo.visible()
                itemView.itemCommentUpdateBtnClo.invisible()
                itemView.itemCommentCancelBtnClo.invisible()
                itemView.itemCommentUpdateEtClo.invisible()
                itemView.itemCommentContentClo.visible()
                itemView.itemCommentContentTv.isFocusable = true
                var newComment = itemView.itemCommentUpdateEt.text.toString()
                updateClickListener(itemView.itemCommentUpdateEt,newComment, comment.id)
            }

            itemView.itemCommentCancelBtnClo.setOnSingleClickListener {
                itemView.itemCommentDeleteBtnClo.visible()
                itemView.itemCommentModifyBtnClo.visible()
                itemView.itemCommentUpdateBtnClo.invisible()
                itemView.itemCommentCancelBtnClo.invisible()
                itemView.itemCommentUpdateEtClo.invisible()
                itemView.itemCommentContentClo.visible()
                itemView.itemCommentContentTv.isFocusable = true
                cancelClickListener(itemView.itemCommentUpdateEt)
            }
        }
    }
}