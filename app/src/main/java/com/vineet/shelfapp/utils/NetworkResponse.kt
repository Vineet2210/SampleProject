package com.vineet.shelfapp.utils

import com.vineet.shelfapp.data.models.ErrorModel


data class NetworkResponse<out T>(val status: Status, val data: T?, val errorModel: ErrorModel?) {
    companion object {

        // In case of Success,set status as
        // Success and data as the response
        fun <T> success(data: T?): NetworkResponse<T> {
            return NetworkResponse(Status.SUCCESS, data, null)
        }

        // In case of failure ,set state to Error ,
        // add the error message,set data to null
        fun <T> error(errorModel: ErrorModel?): NetworkResponse<T> {
            return NetworkResponse(Status.ERROR, null, errorModel)
        }

        // When the call is loading set the state
        // as Loading and rest as null
        fun <T> loading(): NetworkResponse<T> {
            return NetworkResponse(Status.LOADING, null, null)
        }

    }
}

// An enum to store the
// current state of api call
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}