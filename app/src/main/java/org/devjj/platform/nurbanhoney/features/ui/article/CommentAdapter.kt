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
                itemView.comment_modify_btn_holder_clo.invisible()
                itemView.comment_delete_btn_holder_clo.invisible()
            }

            itemView.comment_badge_iv.loadFromUrl(comment.badge, R.drawable.ic_action_no_badge)
            itemView.comment_nickname_tv.text = comment.nickname
            itemView.comment_comment_tv.text = comment.comment
            itemView.comment_delete_btn_holder_clo.setOnSingleClickListener {
                deleteClickListener(comment.id)
            }

            itemView.comment_modify_btn_holder_clo.setOnSingleClickListener {
                itemView.comment_delete_btn_holder_clo.invisible()
                itemView.comment_modify_btn_holder_clo.invisible()
                itemView.comment_update_btn_holder_clo.visible()
                itemView.comment_cancel_btn_holder_clo.visible()
                itemView.comment_update_text_clo.visible()
                itemView.comment_update_text_et.setText(itemView.comment_comment_tv.text)
                itemView.comment_comment_clo.invisible()
                itemView.comment_update_text_et.isFocusable = true
                itemView.comment_update_text_et.requestFocus()
                modifyClickListener(itemView.comment_update_text_et)
            }

            itemView.comment_update_btn_holder_clo.setOnSingleClickListener {
                itemView.comment_delete_btn_holder_clo.visible()
                itemView.comment_modify_btn_holder_clo.visible()
                itemView.comment_update_btn_holder_clo.invisible()
                itemView.comment_cancel_btn_holder_clo.invisible()
                itemView.comment_update_text_clo.invisible()
                itemView.comment_comment_clo.visible()
                itemView.comment_comment_tv.isFocusable = true
                var newComment = itemView.comment_update_text_et.text.toString()
                updateClickListener(itemView.comment_update_text_et,newComment, comment.id)
            }

            itemView.comment_cancel_btn_holder_clo.setOnSingleClickListener {
                itemView.comment_delete_btn_holder_clo.visible()
                itemView.comment_modify_btn_holder_clo.visible()
                itemView.comment_update_btn_holder_clo.invisible()
                itemView.comment_cancel_btn_holder_clo.invisible()
                itemView.comment_update_text_clo.invisible()
                itemView.comment_comment_clo.visible()
                itemView.comment_comment_tv.isFocusable = true
                cancelClickListener(itemView.comment_update_text_et)
            }
        }
    }
}