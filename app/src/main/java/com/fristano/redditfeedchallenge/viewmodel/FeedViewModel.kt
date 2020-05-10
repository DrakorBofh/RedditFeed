package com.fristano.redditfeedchallenge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fristano.redditfeedchallenge.http.NetworkResponse
import com.fristano.redditfeedchallenge.model.Post
import com.fristano.redditfeedchallenge.model.repository.TopRepository
import com.fristano.redditfeedchallenge.viewmodel.helper.SingleLiveEvent
import kotlinx.coroutines.launch

class FeedViewModel(var app: Application) : AndroidViewModel(app) {

    val currentSelectedPost: MutableLiveData<Post?> = MutableLiveData<Post?>()
    val currentTopPostList: MutableLiveData<ArrayList<Post>?> = MutableLiveData<ArrayList<Post>?>()
    val showLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val listItemSelected: SingleLiveEvent<Post?> = SingleLiveEvent()

    fun fetchTop(after: String? = null) {
        showLoading.value = true
        viewModelScope.launch {
            val response = TopRepository.getTop(after)
            showLoading.value = false

            when(response.type) {
                NetworkResponse.ResponseType.SUCCESS -> {
                    val postList:ArrayList<Post> = if (after == null) ArrayList() else currentTopPostList.value ?: ArrayList()

                    response.responseData?.postData?.children?.forEach {
                        postList.add(it.data)
                    }

                    if (currentTopPostList.value == null || currentTopPostList.value!!.isEmpty() )
                        currentSelectedPost.value = postList.first()

                    currentTopPostList.value = postList
                }
                NetworkResponse.ResponseType.FORBIDDEN,
                NetworkResponse.ResponseType.REDIRECT,
                NetworkResponse.ResponseType.ERROR,
                NetworkResponse.ResponseType.JSON_PARSE_ERROR,
                NetworkResponse.ResponseType.SOCKET_CONNECTION_TIMEOUT,
                NetworkResponse.ResponseType.NETWORK_ISSUES,
                NetworkResponse.ResponseType.NO_CONNECTION,
                NetworkResponse.ResponseType.UNKNOWN -> {
                    Log.d("error", response.errorMessage)
                    // TODO show error
                }
            }
        }
    }

    fun fetchNextPage() {
        currentTopPostList.value?.last()?.let {
            fetchTop(it.name)
        }
    }

    fun selectPost(post: Post) {
        post.visited = true
        currentSelectedPost.value = post
        listItemSelected.value = post
    }

    fun removePost(post: Post): Int? {
        val pos = currentTopPostList.value?.indexOf(post) ?: -1
        if (pos > -1) {
            currentTopPostList.value?.removeAt(pos)
            if (pos > 0)
                currentSelectedPost.value = currentTopPostList.value?.get(pos - 1)
            else
                currentSelectedPost.value = currentTopPostList.value?.get(pos)

            return pos
        }
        return null
    }

    fun removeAll() {
        currentTopPostList.value = null
    }
}