package com.fristano.redditfeedchallenge.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fristano.redditfeedchallenge.R
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun ImageView.loadUrlImage(imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .error(R.drawable.ic_account_box_black_48dp)
        .into(this)
}