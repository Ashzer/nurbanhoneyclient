package org.devjj.platform.nurbanhoney.core.controller

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun showKeyboard(activity: Activity, view: View) =
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        view,
        0
    )

fun removeKeyboard(activity: Activity,view: View) =
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        view.windowToken,
        0
    )