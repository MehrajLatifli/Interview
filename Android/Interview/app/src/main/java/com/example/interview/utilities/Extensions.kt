package com.example.interview.utilities

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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




fun EditText.setEllipsizedEditTextIfLongBySize(text: String, size: Int) {
    val words = text.split("\\s+".toRegex()) // Split by whitespace
    if (words.size > size) {
        val truncatedText = words.take(size).joinToString(" ")
        this.setText("$truncatedText...")
    } else {
        this.setText(text)
    }
}



fun ImageView.loadImageWithGlideAndResize(imageId: Int, context: Context) {

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