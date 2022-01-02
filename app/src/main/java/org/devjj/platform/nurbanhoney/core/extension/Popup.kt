package org.devjj.platform.nurbanhoney.core.extension

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

fun getConfirmation(context : Context, msg: String, action: () -> Unit) {
    AlertDialog.Builder(context)
        .setMessage(msg)
        .setPositiveButton("확인") { _, _ ->
            action()
        }
        .setNegativeButton("취소") { _, _ ->
            Toast.makeText(context, "취소 되었습니다", Toast.LENGTH_SHORT).show()
        }
        .show()
}