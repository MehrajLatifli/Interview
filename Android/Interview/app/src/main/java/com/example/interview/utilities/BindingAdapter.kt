package com.example.interview.utilities

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.interview.R

@BindingAdapter("ellipsizedTextIfLong")
fun TextView.setEllipsizedTextIfLong(text: String) {
    if (text.split("\\s+".toRegex()).size >= 10) {
        val words = text.split("\\s+".toRegex()).take(10)
        val truncatedText = words.joinToString(" ")
        this.text = "$truncatedText..."
    } else {
        this.text = text
    }
}


@BindingAdapter("loadImageWithGlide")
fun ImageView.loadImageWithGlide(imageId: String) {
    Glide.with(this.context)
        .load(imageId)
        .apply(RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.color.Transparent)
            .error(R.color.MellowMelon))
        .into(this)
}
