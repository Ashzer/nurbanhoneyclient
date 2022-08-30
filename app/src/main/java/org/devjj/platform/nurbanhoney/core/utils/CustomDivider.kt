package org.devjj.platform.nurbanhoney.core.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class CustomDivider(context: Context, resId: Int) : RecyclerView.ItemDecoration() {
    var divider: Drawable

    init {
        divider = ContextCompat.getDrawable(context, resId)
            ?: throw Exception("No drawable id($resId) exists.")
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        parent.children.forEach {
            val params = it.layoutParams as RecyclerView.LayoutParams
            val top = it.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}