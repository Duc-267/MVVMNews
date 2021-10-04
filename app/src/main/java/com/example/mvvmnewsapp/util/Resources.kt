package com.example.mvvmnewsapp.util

sealed class Resources<T>(
    val data: T? = null,
    val messages: String? = null
) {
    class Success<T>(data: T): Resources<T>(data)
    class Error<T>(messages: String, data: T? = null): Resources<T>(data, messages)
    class Loading<T>(): Resources<T>()

}