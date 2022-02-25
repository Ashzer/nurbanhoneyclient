package org.devjj.platform.nurbanhoney.core.imageprocessing

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrl
import javax.inject.Inject

class ImageViewHandlerImpl
@Inject constructor() : ImageViewHandler {
    override fun createImageView(context: Context) = ImageView(context)

    override fun ImageView.load(url: String, resourceId: Int): ImageView {
        this.loadFromUrl(url, resourceId)
        return this
    }

    override fun ImageView.background(resourceId: Int): ImageView {
        this.background = ContextCompat.getDrawable(this.context, resourceId)
        return this
    }

    override fun ImageView.setPadding(left: Int, top: Int, right: Int, bottom: Int): ImageView {
        this.setPadding(left, top, right, bottom)
        return this
    }

    override fun ImageView.addInto(linearLayout: LinearLayout) {
        linearLayout.addView(this)
    }
}
