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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.interview.R
import java.io.File
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InputStream

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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


fun downloadImage(imageUrl: String, callback: (Bitmap?) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder().url(imageUrl).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            callback(null)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                response.body?.byteStream()?.let { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    callback(bitmap)
                } ?: callback(null)
            } else {
                callback(null)
            }
        }
    })

}



fun ImageView.loadImageWithGlideAndResizeFromUrl(imageUrl: String, context: Context) {

    // Define RequestOptions
    val options = RequestOptions()
        .placeholder(R.color.White) // Replace with actual drawable resource ID
        .error(R.color.MellowMelon) // Replace with actual drawable resource ID
        .dontAnimate()
        .override(500,500)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    try {

//        val replacedUrl = imageUrl.replace("http://host.docker.internal", "http://10.0.2.2")
       val replacedUrl = imageUrl.replace("http://host.docker.internal", "http://192.168.22.189")

        // Download the image



        GlideApp.with(context)
            .asBitmap()
            .load(replacedUrl)
            .apply(options)
            .centerCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    setImageDrawable(placeholder)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    setImageDrawable(errorDrawable)
                    Log.e("ImageLoader1", "Failed to load image: $imageUrl")
                }
            })



    } catch (e: Exception) {
        Log.e("ImageLoader3", "Exception loading image", e)
    }
}





