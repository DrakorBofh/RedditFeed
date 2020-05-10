package com.fristano.redditfeedchallenge.http

import com.google.gson.annotations.SerializedName

open class ApiError {
    companion object {


        fun isErrorCodeIn(errorCode: String?, errorCodeList: List<String>): Boolean {
            for (error in errorCodeList) {
                if (error.equals(errorCode, ignoreCase = true)) return true
            }
            return false
        }

        fun isErrorCodeIn(errorCode: String?, errorCodeToCheck: String): Boolean {
            val errorList = ArrayList<String>()
            errorList.add(errorCodeToCheck)
            return isErrorCodeIn(errorCode, errorList)
        }

    }

    var responseCode: Int? = null

    @SerializedName("code")
    val errorCode: String? = null
    @SerializedName("message")
    val errorMessage: String? = null
    @SerializedName("details")
    val errorDetails: String? = null

    override fun toString(): String {
        return String.format("Code: %s, Message: %s, Details: %s",
            errorCode,
            errorMessage,
            errorDetails
        )
    }

    fun isErrorCode(code: String?): Boolean {
        return errorCode.equals(code, ignoreCase = true)
    }

}
