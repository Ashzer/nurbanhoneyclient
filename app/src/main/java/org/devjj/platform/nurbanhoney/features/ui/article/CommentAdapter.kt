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
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    var userId by Delegates.notNull<Int>()

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
            userId
        )

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            comment: Comment,
            deleteClickListener: (Int) -> Unit,
            updateClickListener: (View, String, Int) -> Unit,
            modifyClickListener: (View) -> Unit,
            cancelClickListener: (View) -> Unit,
            userId: Int
        ) {

            if (userId != comment.userId
            ) {
                itemView.itemCommentModifyBtnClo.invisible()
                itemView.itemCommentDeleteBtnClo.invisible()
            }

            itemView.itemCommentBadgeIv.loadFromUrl(comment.badge, R.drawable.ic_action_no_badge)
            itemView.itemCommentNicknameTv.text = comment.nickname
            itemView.itemCommentContentTv.text = comment.comment

            //댓글 수정 영역 보이기
            commentModifyAreaSetVisibleListener(
                itemView.itemCommentModifyBtnClo,
                modifyClickListener
            )
            //댓글 수정 영역 숨기기(수정 취소)
            commentModifyAreaSetInvisibleListener(
                itemView.itemCommentCancelBtnClo,
                cancelClickListener
            )
            //댓글 수정 버튼(수정 완료)
            commentModifyBtnListener(itemView.itemCommentUpdateBtnClo, updateClickListener, comment)
            //댓글 삭제 버튼
            commentDeleteBtnListener(itemView.itemCommentDeleteBtnClo, deleteClickListener, comment)
        }

        private fun commentModifyAreaSetVisibleListener(view: View, listener: (View) -> Unit) =
            view.setOnSingleClickListener {
                itemView.itemCommentDeleteBtnClo.invisible()
                itemView.itemCommentModifyBtnClo.invisible()
                itemView.itemCommentUpdateBtnClo.visible()
                itemView.itemCommentCancelBtnClo.visible()
                itemView.itemCommentUpdateEtClo.visible()
                itemView.itemCommentUpdateEt.setText(itemView.itemCommentContentTv.text)
                itemView.itemCommentContentClo.invisible()
                itemView.itemCommentUpdateEt.isFocusable = true
                itemView.itemCommentUpdateEt.requestFocus()
                listener(itemView.itemCommentUpdateEt)
            }

        private fun commentModifyAreaSetInvisibleListener(view: View, listener: (View) -> Unit) =
            view.setOnSingleClickListener {
                itemView.itemCommentDeleteBtnClo.visible()
                itemView.itemCommentModifyBtnClo.visible()
                itemView.itemCommentUpdateBtnClo.invisible()
                itemView.itemCommentCancelBtnClo.invisible()
                itemView.itemCommentUpdateEtClo.invisible()
                itemView.itemCommentContentClo.visible()
                itemView.itemCommentContentTv.isFocusable = true
                listener(itemView.itemCommentUpdateEt)
            }

        private fun commentModifyBtnListener(
            view: View,
            listener: (View, String, Int) -> Unit,
            comment: Comment
        ) = view.setOnSingleClickListener {
            itemView.itemCommentDeleteBtnClo.visible()
            itemView.itemCommentModifyBtnClo.visible()
            itemView.itemCommentUpdateBtnClo.invisible()
            itemView.itemCommentCancelBtnClo.invisible()
            itemView.itemCommentUpdateEtClo.invisible()
            itemView.itemCommentContentClo.visible()
            itemView.itemCommentContentTv.isFocusable = true
            var newComment = itemView.itemCommentUpdateEt.text.toString()
            listener(itemView.itemCommentUpdateEt, newComment, comment.id)
        }

        private fun commentDeleteBtnListener(
            view: View,
            listener: (Int) -> Unit,
            comment: Comment
        ) = view.setOnSingleClickListener {
            listener(comment.id)
        }

    }
}