package com.fristano.redditfeedchallenge.model

import com.google.gson.annotations.SerializedName

data class ResponseData (

    @SerializedName("modhash") val modhash : String,
    @SerializedName("children") val children : List<Children>,
    @SerializedName("after") val after : String,
    @SerializedName("before") val before : String
)