package org.devjj.platform.nurbanhoney.core.extension

import android.Manifest
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.viewbinding.ViewBinding
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.text_editor_body.view.*
import org.devjj.platform.nurbanhoney.core.permission.askReadStoragePermission
import org.devjj.platform.nurbanhoney.core.permission.isNotPermissionsAllowed

fun RichEditor.setTextEditorListeners(binding: ViewBinding) {
    this.undoListener(binding)
    this.redoListener(binding)
    this.boldListener(binding)
    this.italicListener(binding)
    this.underlineListener(binding)
    this.alignLeftListener(binding)
    this.alignCenterListener(binding)
    this.alignRightListener(binding)
}

fun RichEditor.undoListener(binding: ViewBinding) =
    binding.root.action_undo.setOnClickListener { this.undo() }

fun RichEditor.redoListener(binding: ViewBinding) =
    binding.root.action_redo.setOnClickListener { this.redo() }

fun RichEditor.boldListener(binding: ViewBinding) =
    binding.root.action_bold.setOnClickListener { this.setBold() }

fun RichEditor.italicListener(binding: ViewBinding) =
    binding.root.action_italic.setOnClickListener { this.setItalic() }

fun RichEditor.underlineListener(binding: ViewBinding) =
    binding.root.action_underline.setOnClickListener { this.setUnderline() }

fun RichEditor.alignLeftListener(binding: ViewBinding) =
    binding.root.action_align_left.setOnClickListener { this.setAlignLeft() }

fun RichEditor.alignCenterListener(binding: ViewBinding) =
    binding.root.action_align_center.setOnClickListener { this.setAlignCenter() }

fun RichEditor.alignRightListener(binding: ViewBinding) =
    binding.root.action_align_right.setOnClickListener { this.setAlignRight() }

fun RichEditor.insertImageListener(
    view: View,
    activity: Activity,
    cropActivityResultLauncher: ActivityResultLauncher<Any?>
) =
    view.setOnClickListener {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        if (isNotPermissionsAllowed(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) askReadStoragePermission(activity)

        cropActivityResultLauncher.launch(null)
    }

fun functionVisibility(displayView: View, functionView: View) =
    displayView.setOnFocusChangeListener { _, hasFocus ->
        when (hasFocus) {
            true -> functionView.visible()
            false -> functionView.invisible()
        }
    }