package org.devjj.platform.nurbanhoney.core.imageprocessing

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import javax.inject.Inject

interface ImageViewHandler {
    fun createImageView(context : Context) : ImageView
    fun ImageView.load(url : String, resourceId : Int) : ImageView
    fun ImageView.background(resourceId: Int) : ImageView
    fun ImageView.setPadding(left : Int, top : Int , right : Int , bottom : Int ) : ImageView
    fun ImageView.addInto(linearLayout: LinearLayout)
}