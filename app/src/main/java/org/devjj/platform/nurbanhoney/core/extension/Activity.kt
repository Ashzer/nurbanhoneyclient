package org.devjj.platform.nurbanhoney.core.extension

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun showKeyboard(activity: Activity) =
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        0
    )

fun removeKeyboard(activity: Activity) =
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )