package com.fristano.redditfeedchallenge.http

import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

object ApiCall {

    private val sResponsesSuccess = arrayOf(200, 201, 204)
    private val sResponsesForbidden = arrayOf(401, 403, 422)

    suspend fun <T>safeApiResult(dispatcher: CoroutineDispatcher = Dispatchers.IO, call: suspend () -> T): NetworkResponse<T> {
        return withContext(dispatcher) {
            try {
                val response = call.invoke()
                NetworkResponse.createSuccess(response)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> NetworkResponse.createNetworkIssues<T>()
                    is SocketTimeoutException -> NetworkResponse.createTimeout<T>()
                    is HttpException -> processHttpError<T>(throwable)
                    is JsonSyntaxException -> {
                        NetworkResponse.createParseError(throwable.message)
                    }
                    else -> {
                        NetworkResponse.createNoConnection<T>()
                    }
                }
            }
        }
    }

    private fun <T>processHttpError(throwable: HttpException): NetworkResponse<T>  {
        val response = throwable.response()
        if (response?.code() == null)
            return NetworkResponse.createNoConnection()

        if (response.code() > 99 && response.code() / 100 == 3) {
            return response.headers()["Location"]?.let {
                NetworkResponse.createRedirect<T>(it)
            } ?: NetworkResponse.createRedirectToNoWhere<T>()
        } else {
            for (respForbidden in sResponsesForbidden) {
                if (respForbidden == response.code()) {
                    return NetworkResponse.createForbidden<T>(parseError(response))
                }
            }
            return NetworkResponse.createError<T>(parseError(response))
        }
    }

    private fun parseError(response: Response<*>): ApiError? {
        val converter = ApiConfig.simpleClient
            .responseBodyConverter<ApiError>(ApiError::class.java, arrayOfNulls(0))
        if (response.errorBody() == null) return null

        val error: ApiError?
        error = try {
            converter.convert(response.errorBody()!!)
        } catch (e: Exception) {
            return ApiError()
        }
        error?.responseCode = response.code()
        return error
    }
}
