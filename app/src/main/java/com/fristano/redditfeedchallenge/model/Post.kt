package com.fristano.redditfeedchallenge.model

import com.google.gson.annotations.SerializedName
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

data class Post (

    @SerializedName("domain") val domain : String,
    @SerializedName("banned_by") val banned_by : String,
    @SerializedName("subreddit") val subreddit : String,
    @SerializedName("selftext_html") val selftext_html : String,
    @SerializedName("selftext") val selftext : String,
    @SerializedName("likes") val likes : String,
    @SerializedName("user_reports") val user_reports : List<String>,
    @SerializedName("link_flair_text") val link_flair_text : String,
    @SerializedName("id") val id : String,
    @SerializedName("gilded") val gilded : Int,
    @SerializedName("clicked") val clicked : Boolean,
    @SerializedName("report_reasons") val report_reasons : String,
    @SerializedName("author") val author : String,
    @SerializedName("score") val score : Int,
    @SerializedName("approved_by") val approved_by : String,
    @SerializedName("over_18") val over_18 : Boolean,
    @SerializedName("hidden") val hidden : Boolean,
    @SerializedName("thumbnail") val thumbnail : String,
    @SerializedName("subreddit_id") val subreddit_id : String,
    @SerializedName("link_flair_css_class") val link_flair_css_class : String,
    @SerializedName("author_flair_css_class") val author_flair_css_class : String,
    @SerializedName("downs") val downs : Int,
    @SerializedName("mod_reports") val mod_reports : List<String>,
    @SerializedName("saved") val saved : Boolean,
    @SerializedName("is_self") val is_self : Boolean,
    @SerializedName("name") val name : String,
    @SerializedName("permalink") val permalink : String,
    @SerializedName("stickied") val stickied : Boolean,
    @SerializedName("created") val created : Long,
    @SerializedName("url") val url : String,
    @SerializedName("author_flair_text") val author_flair_text : String,
    @SerializedName("title") val title : String,
    @SerializedName("created_utc") val created_utc : Long,
    @SerializedName("ups") val ups : Int,
    @SerializedName("num_comments") val numComments : Int = 0,
    @SerializedName("visited") var visited : Boolean = false,
    @SerializedName("num_reports") val num_reports : String,
    @SerializedName("distinguished") val distinguished : String
) {
    val createdTimeText: String
        get() = PrettyTime().format(Date(created_utc*1000))
}