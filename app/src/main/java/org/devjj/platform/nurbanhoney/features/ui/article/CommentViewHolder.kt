package org.devjj.platform.nurbanhoney.features.ui.article

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comment.view.*
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.invisible
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrl
import org.devjj.platform.nurbanhoney.core.extension.setOnSingleClickListener
import org.devjj.platform.nurbanhoney.core.extension.visible
import org.devjj.platform.nurbanhoney.core.sharedpreference.Prefs
import org.devjj.platform.nurbanhoney.databinding.ItemCommentBinding
import org.devjj.platform.nurbanhoney.features.ui.article.model.Comment

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var binding = ItemCommentBinding.bind(itemView)
    fun bind(
        comment: Comment,
        deleteClickListener: (Int) -> Unit,
        updateClickListener: (View, String, Int) -> Unit,
        modifyClickListener: (View) -> Unit,
        cancelClickListener: (View) -> Unit
    ) {

        if (Prefs.userId != comment.userId
        ) {
            binding.itemCommentModifyBtnClo.invisible()
            binding.itemCommentDeleteBtnClo.invisible()
        } else {
            binding.itemCommentModifyBtnClo.visible()
            binding.itemCommentDeleteBtnClo.visible()
        }

        binding.itemCommentBadgeIv.loadFromUrl(comment.badge, R.drawable.ic_action_no_badge)
        binding.itemCommentNicknameTv.text = comment.nickname
        binding.itemCommentContentTv.text = comment.comment

        binding.itemCommentInsigniaLlo.removeAllViews()
        comment.insignia.forEach {
            addInsigniaImage(it)
        }


        //댓글 수정 영역 보이기
        commentModifyAreaSetVisibleListener(
            binding.itemCommentModifyBtnClo,
            modifyClickListener
        )
        //댓글 수정 영역 숨기기(수정 취소)
        commentModifyAreaSetInvisibleListener(
            binding.itemCommentCancelBtnClo,
            cancelClickListener
        )
        //댓글 수정 버튼(수정 완료)
        commentModifyBtnListener(binding.itemCommentUpdateBtnClo, updateClickListener, comment)
        //댓글 삭제 버튼
        commentDeleteBtnListener(binding.itemCommentDeleteBtnClo, deleteClickListener, comment)

        itemView.setOnSingleClickListener {
            Log.d("comment_individual__", comment.toString())
        }
    }

    // 휘장 이미지 셋팅하는 메소드
    private fun addInsigniaImage(url: String) {
        val iv = ImageView(itemView.context)
        with(iv) {
            loadFromUrl(url, R.drawable.ic_action_no_badge)
            setMargins(iv, 0, 0, 5, 0)
            //background = ContextCompat.getDrawable(itemView.context, R.drawable.edges_rectangle)
           // setPadding(5, 5, 5, 5)
            itemView.itemCommentInsigniaLlo.addView(this)
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        var gl = GridLayout.LayoutParams()
        gl.setMargins(left, top, right, bottom)
        view.layoutParams = gl
    }

    private fun commentModifyAreaSetVisibleListener(view: View, listener: (View) -> Unit) =
        view.setOnSingleClickListener {
            with(binding) {
                itemCommentDeleteBtnClo.invisible()
                itemCommentModifyBtnClo.invisible()
                itemCommentUpdateBtnClo.visible()
                itemCommentCancelBtnClo.visible()
                itemCommentUpdateEtClo.visible()
                itemCommentUpdateEt.setText(itemCommentContentTv.text)
                itemCommentContentClo.invisible()
                itemCommentUpdateEt.requestFocus()
                itemCommentUpdateEt.isFocusable = true
            }
            listener(binding.itemCommentUpdateEt)
        }

    private fun commentModifyAreaSetInvisibleListener(view: View, listener: (View) -> Unit) =
        view.setOnSingleClickListener {
            binding.itemCommentDeleteBtnClo.visible()
            binding.itemCommentModifyBtnClo.visible()
            binding.itemCommentUpdateBtnClo.invisible()
            binding.itemCommentCancelBtnClo.invisible()
            binding.itemCommentUpdateEtClo.invisible()
            binding.itemCommentContentClo.visible()
            binding.itemCommentContentTv.isFocusable = true
            listener(binding.itemCommentUpdateEt)
        }

    private fun commentModifyBtnListener(
        view: View,
        listener: (View, String, Int) -> Unit,
        comment: Comment
    ) = view.setOnSingleClickListener {
        binding.itemCommentDeleteBtnClo.visible()
        binding.itemCommentModifyBtnClo.visible()
        binding.itemCommentUpdateBtnClo.invisible()
        binding.itemCommentCancelBtnClo.invisible()
        binding.itemCommentUpdateEtClo.invisible()
        binding.itemCommentContentClo.visible()
        binding.itemCommentContentTv.isFocusable = true
        var newComment = binding.itemCommentUpdateEt.text.toString()
        listener(binding.itemCommentUpdateEt, newComment, comment.id)
    }

    private fun commentDeleteBtnListener(
        view: View,
        listener: (Int) -> Unit,
        comment: Comment
    ) = view.setOnSingleClickListener {
        listener(comment.id)
    }

}