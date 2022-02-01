package org.devjj.platform.nurbanhoney.core.extension

import android.graphics.PorterDuff
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.devjj.platform.nurbanhoney.R

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
    Glide.with(this.context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(resourceId)
        .error(resourceId)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)

fun ImageView.loadFromUrlWithSize(url: String, resourceId: Int, width: Int, height : Int) =
    Glide.with(this.context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(resourceId)
        .error(resourceId)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .override(width, height)
        .into(this)

fun ImageView.loadFromDrawable(resourceId: Int) =
    Glide.with(this.context.applicationContext)
        .load(resourceId)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)

fun View.setOnSingleClickListener(debounceTime: Long = 500L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {

            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

