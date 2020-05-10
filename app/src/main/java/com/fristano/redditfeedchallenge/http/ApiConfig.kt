package com.fristano.redditfeedchallenge.http

import android.os.Build
import android.util.Log
import com.fristano.redditfeedchallenge.BuildConfig
import com.google.gson.GsonBuilder
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val LOG_TAG = "ApiConfig"
    private const val REDDIT_URL_BASE = "https://www.reddit.com"
    private const val TIMEOUT_CONNECTION: Long = 10

    enum class TimeoutType(val timeoutSecs: Long) {
        SHORT_TIMEOUT(10),
        LONG_TIMEOUT(50)
    }

    private val userAgent = String.format("RedditFeedChallengeApp %s %s %s %s %s", "android", Build.MODEL, Build.MANUFACTURER, Build.VERSION.RELEASE, "01")

    private fun getClient(baseUrl: String, followRedirect: Boolean,
                          timeoutType: TimeoutType?) : Retrofit {
        val timeoutSecs = timeoutType?.timeoutSecs ?: TimeoutType.SHORT_TIMEOUT.timeoutSecs
        val httpClient = OkHttpClient.Builder()

        httpClient.connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
            .writeTimeout(timeoutSecs, TimeUnit.SECONDS)
            .readTimeout(timeoutSecs, TimeUnit.SECONDS)

        httpClient.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", userAgent)

            chain.proceed(request.build())
        })

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(loggingInterceptor)

            httpClient.addInterceptor(CurlInterceptor(Loggable {
                    message -> Log.d(LOG_TAG, message)
            }))
        }

        httpClient.followRedirects(followRedirect)
        httpClient.followSslRedirects(followRedirect)
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }

    private fun getClient(baseUrl: String, timeoutType: TimeoutType?): Retrofit {
        return getClient(baseUrl, true, timeoutType)
    }

    private fun getSimpleClient(baseUrl: String, timeoutType: TimeoutType?): Retrofit {
        return getClient(baseUrl, timeoutType)
    }

    val simpleClient: Retrofit
        get() = getSimpleClient(REDDIT_URL_BASE, TimeoutType.SHORT_TIMEOUT)

    val simpleLongResponseClient: Retrofit
        get() = getSimpleClient(REDDIT_URL_BASE, TimeoutType.LONG_TIMEOUT)

}
