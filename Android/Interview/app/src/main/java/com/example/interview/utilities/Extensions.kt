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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

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
        .placeholder(R.drawable.defaultimage)
        .error(R.drawable.errorimage)
        .into(this)
}

fun sha512(input: String): String {
    val md = MessageDigest.getInstance("SHA-512")
    val hashBytes = md.digest(input.toByteArray())
    return Base64.getEncoder().encodeToString(hashBytes)
}

suspend fun encryptWithAsync(plainText: String, key: SecretKey): Pair<String, String> = withContext(Dispatchers.Default) {
    val iv = IvParameterSpec(key.encoded.copyOf(16))
    val plainBytes = plainText.toByteArray()

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)

    val encryptedBytes = cipher.doFinal(plainBytes)
    val encryptedText = Base64.getEncoder().encodeToString(encryptedBytes)

    val hash = sha512(plainText)

    Pair(encryptedText, hash)
}

suspend fun decryptWithAsync(encryptedText: String, hash: String, key: SecretKey): String? = withContext(Dispatchers.Default) {
    val iv = IvParameterSpec(key.encoded.copyOf(16))
    val encryptedBytes = Base64.getDecoder().decode(encryptedText)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, iv)

    val decryptedBytes = cipher.doFinal(encryptedBytes)
    val decryptedText = String(decryptedBytes)

    val computedHash = sha512(decryptedText)

    if (computedHash == hash) {
        decryptedText
    } else {
        null
    }
}

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}