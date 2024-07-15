package com.example.interview.utilities


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.interview.R
import java.io.File


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

fun ImageView.loadImageWithGlideAndResizeFromUrl(imageUrl: String, context: Context) {
    // Define RequestOptions
    val options = RequestOptions()
        .placeholder(R.color.Transparent) // Use actual placeholder resource ID
        .error(R.color.MellowMelon) // Use actual error resource ID
        .override(50, 50) // Optional resizing
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    try {
        // Build URI if needed


        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .apply(options)
            .into(object : CustomTarget<Bitmap>(width, height) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    setImageDrawable(placeholder)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    setImageDrawable(errorDrawable)
                    Log.e(
                        "ImageLoader",
                        "Failed to load image: $imageUrl"
                    )
                }
            })
    } catch (e: Exception) {
        Log.e("ImageLoader", "Exception loading image", e)
    }
}


fun ImageView.loadUrl(url: String) {

    val imageLoader = ImageLoader.Builder(this.context)
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.registerstatuse)
        .error(R.drawable.registerstatuse)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}


