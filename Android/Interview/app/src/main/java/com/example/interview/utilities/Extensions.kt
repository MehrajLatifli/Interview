package com.example.interview.utilities

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

fun ImageView.loadImageWithoutBindingAdapter(url: String?) {
    Glide
        .with(this)
        .load(url)
        .centerCrop().placeholder(R.color.black)
        .into(this)
}



fun String.highlightName(fragment: Fragment, name: String, colorResId: Int): SpannableString {
    val spannableString = SpannableString(this)
    val start = this.indexOf(name)
    if (start != -1) {
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(fragment.requireContext(), colorResId)),
            start,
            start + name.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannableString
}

fun loadimagewithidWithoutBindingAdapter(imageView: ImageView, imageId: Int) {

    imageView.setImageResource(imageId)
}

fun ImageView.loadImageWithGlideAndResize(imageId: Int, context: Context) {

    Glide.with(context)
        .clear(this)

    Glide.with(context)
        .load(imageId)
        .apply(RequestOptions())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.color.white)
        .error(R.color.transparent)
        .into(this)
}


fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}