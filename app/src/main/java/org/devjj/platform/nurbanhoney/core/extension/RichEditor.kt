package org.devjj.platform.nurbanhoney.core.extension

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import jp.wasabeef.richeditor.RichEditor
import org.devjj.platform.nurbanhoney.core.permission.STORAGE_PERMISSION_REQUEST_CODE
import org.devjj.platform.nurbanhoney.core.permission.askReadStoragePermission
import org.devjj.platform.nurbanhoney.core.permission.isNotPermissionsAllowed

fun RichEditor.undoListener(view: View) =
    view.setOnClickListener { this.undo() }

fun RichEditor.redoListener(view: View) =
    view.setOnClickListener { this.redo() }

fun RichEditor.boldListener(view: View) =
    view.setOnClickListener { this.setBold() }

fun RichEditor.italicListener(view: View) =
    view.setOnClickListener { this.setItalic() }

fun RichEditor.underlineListener(view: View) =
    view.setOnClickListener { this.setUnderline() }

fun RichEditor.alignLeftListener(view: View) =
    view.setOnClickListener { this.setAlignLeft() }

fun RichEditor.alignCenterListener(view: View) =
    view.setOnClickListener { this.setAlignCenter() }

fun RichEditor.alignRightListener(view: View) =
    view.setOnClickListener { this.setAlignRight() }

fun RichEditor.insertImageListener(
    view: View,
    activity: Activity,
    cropActivityResultLauncher: ActivityResultLauncher<Any?>
) =
    view.setOnClickListener {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)

        Log.d(
            "permission_check__",
            isNotPermissionsAllowed(context, Manifest.permission.READ_EXTERNAL_STORAGE).toString()
        )

        if (isNotPermissionsAllowed(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) askReadStoragePermission(activity)

        cropActivityResultLauncher.launch(null)
    }

fun functionVisibility(displayView : View, functionView :View) =
    displayView.setOnFocusChangeListener { _, hasFocus ->
        when(hasFocus){
            true->functionView.visible()
            false->functionView.invisible()
        }
    }