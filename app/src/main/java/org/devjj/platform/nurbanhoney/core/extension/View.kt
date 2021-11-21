package org.devjj.platform.nurbanhoney.core.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ImageView.loadFromUrl(url: String, resourceId: Int) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(resourceId)
        .error(resourceId)
        .into(this)