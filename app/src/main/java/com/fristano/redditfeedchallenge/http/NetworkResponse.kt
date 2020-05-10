package com.fristano.redditfeedchallenge.http

class NetworkResponse<T> {
    enum class ResponseType() {
        SUCCESS,
        FORBIDDEN,
        REDIRECT,
        ERROR,
        JSON_PARSE_ERROR,
        SOCKET_CONNECTION_TIMEOUT,
        NETWORK_ISSUES,
        NO_CONNECTION,
        UNKNOWN
    }

    var type = ResponseType.UNKNOWN
    var responseData: T? = null
    var error: ApiError? = null
    var url: String? = null
    var errorMessage: String? = null

    constructor(status: ResponseType, data: T?) {
        this.type = status
        this.responseData = data
    }

    constructor(status: ResponseType, error: ApiError?) {
        this.type = status
        this.error = error
    }

    constructor(status: ResponseType, url: String) {
        this.type = status
        this.url = url
    }

    constructor(status: ResponseType) {
        this.type = status
    }


    companion object {

        fun <T> createSuccess(data: T): NetworkResponse<T> {
            return NetworkResponse(ResponseType.SUCCESS, data)
        }

        fun <T> createForbidden(error: ApiError?): NetworkResponse<T> {
            return NetworkResponse(ResponseType.FORBIDDEN, error)
        }

        fun <T> createError(error: ApiError?): NetworkResponse<T> {
            return NetworkResponse(ResponseType.ERROR, error)
        }

        fun <T> createRedirect(url: String): NetworkResponse<T> {
            return NetworkResponse(ResponseType.REDIRECT, url)
        }

        fun <T> createRedirectToNoWhere(): NetworkResponse<T> {
            return NetworkResponse<T>(ResponseType.REDIRECT)
        }

        fun <T> createTimeout(): NetworkResponse<T> {
            return NetworkResponse<T>(ResponseType.SOCKET_CONNECTION_TIMEOUT, null)
        }

        fun <T> createNetworkIssues(): NetworkResponse<T> {
            return NetworkResponse<T>(ResponseType.NETWORK_ISSUES, null)
        }

        fun <T> createNoConnection(): NetworkResponse<T> {
            return NetworkResponse<T>(ResponseType.NO_CONNECTION, null)
        }

        fun <T> createParseError(message: String?): NetworkResponse<T> {
            val response = NetworkResponse<T>(ResponseType.JSON_PARSE_ERROR)
            response.errorMessage = message
            return response
        }
    }

}