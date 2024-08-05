package com.example.interview.utilities

import android.content.Context
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.interview.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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



@BindingAdapter("formatDateTimeStart", "formatDateTimeEnd")
fun TextView.formatDateTime(startDate: String?, endDate: String?) {
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss.SSS")

    val formattedStartDate = try {
        if (startDate.isNullOrEmpty()) {
            "null"
        } else {
            val dateTime = LocalDateTime.parse(startDate, inputFormatter)
            dateTime.format(outputFormatter)
        }
    } catch (e: Exception) {
        "Invalid Start Date Format"
    }

    val formattedEndDate = try {
        if (endDate.isNullOrEmpty()) {
            "null"
        } else {
            val dateTime = LocalDateTime.parse(endDate, inputFormatter)
            dateTime.format(outputFormatter)
        }
    } catch (e: Exception) {
        "Invalid End Date Format"
    }

    this.text = "Start date: \t ${formattedStartDate} \nEnd date:  \t ${formattedEndDate}"
}



