package com.fristano.redditfeedchallenge.http

import com.fristano.redditfeedchallenge.http.endpoint.Top
import com.fristano.redditfeedchallenge.model.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object ApiRequest {
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getTop(subreddit: String?, options: Map<String?, String?>?): NetworkResponse<Response?> {

        val apiConfig = ApiConfig.simpleClient.create(Top::class.java)

        return ApiCall.safeApiResult(dispatcher, call = {
            apiConfig.getTop(subreddit, options?.toMap())
        } )
    }
}