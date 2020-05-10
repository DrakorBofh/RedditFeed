package com.fristano.redditfeedchallenge.http.endpoint

import com.fristano.redditfeedchallenge.model.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface Top {

    @GET("/r/{subreddit}/top.json")
    suspend fun getTop(@Path("subreddit") subreddit: String?,
                             @QueryMap options: Map<String?, String?>?): Response?
}