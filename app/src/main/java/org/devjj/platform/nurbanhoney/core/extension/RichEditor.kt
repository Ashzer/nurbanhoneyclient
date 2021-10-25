package org.devjj.platform.nurbanhoney.core.extension

import android.view.View
import jp.wasabeef.richeditor.RichEditor

fun undoListener(view : View , editor: RichEditor) =
    view.setOnClickListener { editor.undo() }
fun redoListener(view : View , editor: RichEditor) =
    view.setOnClickListener { editor.redo() }
fun boldListener(view : View , editor: RichEditor) =
    view.setOnClickListener { editor.setBold() }
fun italicListener(view : View , editor : RichEditor) =
    view.setOnClickListener { editor.setItalic() }
fun underlineListener(view : View , editor : RichEditor) =
    view.setOnClickListener { editor.setUnderline() }
fun alignLeftListener(view : View , editor : RichEditor) =
    view.setOnClickListener { editor.setAlignLeft() }
fun alignCenterListener(view : View , editor : RichEditor) =
    view.setOnClickListener { editor.setAlignCenter() }
fun alignRightListener(view : View , editor : RichEditor) =
    view.setOnClickListener { editor.setAlignRight() }