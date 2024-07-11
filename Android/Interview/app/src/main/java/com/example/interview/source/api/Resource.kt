package com.example.interview.source.api

import com.example.interview.models.responses.get.error.ErrorResponse

sealed class Resource<T>(val data: T? = null, val message: ErrorResponse? = null) {

    data class Success<T>(val dataSuccess: T?) : Resource<T>(dataSuccess)

    data class Error<T>(val errorResponse: ErrorResponse?) : Resource<T>(null, errorResponse)
}
