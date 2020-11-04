package com.mobiquity.miniapp.utils

import android.content.res.Resources.getSystem
import android.widget.ImageView
import com.mobiquity.miniapp.R
import com.mobiquity.miniapp.model.BASE_URL
import com.mobiquity.miniapp.model.entities.Product
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

fun ImageView.setProductImage(item: Product?, width: Float, height: Float) {
    item?.let {
        loadAndSetImage(item.url, width.toInt(), height.toInt(), this)
    }
}

fun loadAndSetImage(
    url: String,
    width: Int,
    height: Int,
    imageView: ImageView
) {
    Picasso.get()
        .load(BASE_URL + url)
        .networkPolicy(NetworkPolicy.NO_CACHE)
        .placeholder(R.drawable.ic_pic_placeholder)
        .resize(width.px, height.px)
        .into(imageView)
}

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
