package com.fristano.redditfeedchallenge.model.repository

import com.fristano.redditfeedchallenge.http.ApiRequest
import com.fristano.redditfeedchallenge.http.NetworkResponse
import com.fristano.redditfeedchallenge.model.Post
import com.fristano.redditfeedchallenge.model.Response

object TopRepository {

    suspend fun getTop(after: String?): NetworkResponse<Response?> {

        val options: HashMap<String, String> = HashMap()
        options["raw_json"] = "1"
        options["limit"] = "10"
        after?.let { options["after"] = it }

        return ApiRequest.getTop("funny", options.toMap())
    }


}