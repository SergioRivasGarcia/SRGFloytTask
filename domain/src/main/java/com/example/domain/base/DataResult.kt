package com.example.domain.base

sealed class DataResult<out R> {

    enum class DataStatus {
        LOADING,
        COMPLETED
    }

    data class Success<out T>(val data: T?) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>()
    data class Status(val status: DataStatus) : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Status -> "Status[status=$status]"
        }
    }
}
