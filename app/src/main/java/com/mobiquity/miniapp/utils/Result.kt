package com.mobiquity.miniapp.utils

sealed class Result<out T> {

    abstract val status: Status
    abstract val data: T?
    abstract val message: String?

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    class Success<T>(override val data: T) : Result<T>() {
        override val status = Status.SUCCESS
        override val message: String? = null
    }

    class Error<T>(override val message: String?, override val data: T? = null) : Result<T>() {
        override val status = Status.ERROR
    }

    class Loading<T>(override val data: T? = null) : Result<T>() {
        override val status = Status.LOADING
        override val message: String? = null
    }
}