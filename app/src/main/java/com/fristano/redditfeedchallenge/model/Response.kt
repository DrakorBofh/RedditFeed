package com.fristano.redditfeedchallenge.model

import com.google.gson.annotations.SerializedName

data class Response (

    @SerializedName("kind") val kind : String,
    @SerializedName("data") val postData : ResponseData
)