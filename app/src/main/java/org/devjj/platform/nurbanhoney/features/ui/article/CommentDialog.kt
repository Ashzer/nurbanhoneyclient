package org.devjj.platform.nurbanhoney.features.ui.article

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.databinding.DialogCommentBinding

class CommentDialog : DialogFragment() {
    private var _binding: DialogCommentBinding? = null
    private val binding get() = _binding!!

    lateinit var commentUploadListener: CommentDialogUploadListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogCommentSaveBtn.setOnClickListener {
            commentUploadListener.uploadComment(binding.dialogAddTodoContentEt.text.toString())
            dismiss()
        }

        binding.dialogCommentCancelBtn.setOnClickListener {
            dismiss()
        }

        var commentMaxLines = resources.getInteger(R.integer.comment_max_lines)
        setCommentsLimitLines(binding.dialogAddTodoContentEt, commentMaxLines)
        binding.dialogAddTodoContentEt.hint = getString(
            R.string.comment_max_lines_hint,
            resources.getInteger(R.integer.comment_max_length),
            commentMaxLines
        )
    }

    private fun setCommentsLimitLines(editText: EditText, maxLines: Int) =
        editText.addTextChangedListener(object : TextWatcher {
            var previousString = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                previousString = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.dialogAddTodoContentEt.lineCount > maxLines) {
                    binding.dialogAddTodoContentEt.setText(previousString)
                    binding.dialogAddTodoContentEt.setSelection(binding.dialogAddTodoContentEt.length())
                }
            }
        })

    interface CommentDialogUploadListener {
        fun uploadComment(comment: String)
    }

    fun uploadComment(listener: CommentDialogUploadListener) {
        commentUploadListener = listener
    }

}