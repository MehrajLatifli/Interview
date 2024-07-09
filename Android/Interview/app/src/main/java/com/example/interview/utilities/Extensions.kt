package com.example.interview.utilities

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.interview.R

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun ImageView.loadImageWithGlideAndResize(imageId: String, context: Context) {

    Glide.with(context)
        .clear(this)

    Glide.with(context)
        .load(imageId)
        .apply(RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.color.Transparent)
            .error(R.color.MellowMelon))
        .into(this)
}