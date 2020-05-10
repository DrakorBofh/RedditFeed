package com.fristano.redditfeedchallenge.extension

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fristano.redditfeedchallenge.R
import okhttp3.internal.format

@BindingAdapter("commentNumberFormat")
fun TextView.commentNumberFormat(value: Int?) {
    value?.let {
        text = format("%d %s", value, context.getString(R.string.comments))
    } ?: run {visibility = View.GONE}
}