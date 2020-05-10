package com.fristano.redditfeedchallenge.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


object FileHelper {

    fun imageDownload(context: Context, url: String?) {
        Picasso.get()
            .load(url)
            .into(getTarget(context, String.format("%d.jpg", System.currentTimeMillis())))
    }

    //target to save
    private fun getTarget(context: Context, filename: String): Target {
        return object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom?) {
                Thread(Runnable {
                    val file = File(
                        context.externalMediaDirs.first().absolutePath + "/" + filename
                    )
                    try {
                        file.createNewFile()
                        val ostream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                        ostream.flush()
                        ostream.close()
                    } catch (e: IOException) {
                        Log.e("IOException", e.localizedMessage)
                    }
                }).start()
            }

            fun onBitmapFailed(errorDrawable: Drawable?) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            }
        }
    }
}