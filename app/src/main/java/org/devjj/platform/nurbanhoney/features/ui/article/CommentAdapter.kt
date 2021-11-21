package org.devjj.platform.nurbanhoney.features.ui.article

import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comment.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inflate
import org.devjj.platform.nurbanhoney.core.extension.invisible
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrl
import org.devjj.platform.nurbanhoney.core.extension.visible
import javax.inject.Inject
import kotlin.properties.Delegates

class CommentAdapter
@Inject constructor(
    private val prefs: SharedPreferences,
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    internal var collection: List<Comment> by Delegates.observable(emptyList()) { _, old, new ->
        //notifyItemInserted(old.size)
        notifyDataSetChanged()
    }

    internal var deleteClickListener: (Int) -> Unit = { _ -> }
    internal var updateClickListener: (Int) -> Unit = { _ -> }
    internal var insertClickListener: () -> Unit = {}
    internal var cancelClickListener: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_comment))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(
            collection[position],
            deleteClickListener,
            updateClickListener,
            insertClickListener,
            cancelClickListener,
            prefs
        )

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            comment: Comment,
            deleteClickListener: (Int) -> Unit,
            updateClickListener: (Int) -> Unit,
            insertClickListener: () -> Unit,
            cancelClickListener: () -> Unit,
            prefs: SharedPreferences
        ) {

            if (prefs.getString(R.string.prefs_user_id.toString(), "-1")
                    ?.toInt() ?: -1 != comment.userId
            ) {
                itemView.comment_insert_btn.invisible()
                itemView.comment_delete_btn.invisible()
            }

            itemView.comment_badge_iv.loadFromUrl(comment.badge, R.drawable.ic_action_no_badge)
            itemView.comment_nickname_tv.text = comment.nickname
            itemView.comment_comment_tv.text = comment.comment
            itemView.comment_delete_btn.setOnClickListener {
                deleteClickListener(comment.id)
            }

            itemView.comment_insert_btn.setOnClickListener {
                itemView.comment_delete_btn.invisible()
                itemView.comment_insert_btn.invisible()
                itemView.comment_update_btn.visible()
                itemView.comment_cancel_btn.visible()
                insertClickListener()
            }

            itemView.comment_update_btn.setOnClickListener {
                itemView.comment_delete_btn.visible()
                itemView.comment_insert_btn.visible()
                itemView.comment_update_btn.invisible()
                itemView.comment_cancel_btn.invisible()
                updateClickListener(comment.id)
            }

            itemView.comment_cancel_btn.setOnClickListener {
                itemView.comment_delete_btn.visible()
                itemView.comment_insert_btn.visible()
                itemView.comment_update_btn.invisible()
                itemView.comment_cancel_btn.invisible()
                cancelClickListener()
            }
        }
    }
}