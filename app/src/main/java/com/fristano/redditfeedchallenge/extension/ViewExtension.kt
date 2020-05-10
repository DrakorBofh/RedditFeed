package com.fristano.redditfeedchallenge.extension

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("goneIfNull")
fun View.goneIfNull(value: Any?) {
    visibility = if (value == null) View.GONE else View.VISIBLE
}

@BindingAdapter("goneIfTrue")
fun View.goneIfTrue(hide: Boolean) {
    visibility = if (hide) View.GONE else View.VISIBLE
}
