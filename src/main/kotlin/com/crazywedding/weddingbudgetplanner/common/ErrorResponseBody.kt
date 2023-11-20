package com.crazywedding.weddingbudgetplanner.common

import com.crazywedding.weddingbudgetplanner.common.error.exception.BaseException

class ErrorResponseBody(
    val errorCode: String,
    val errorMessage: String,
) {
    companion object {
        fun of(exception: BaseException) = ErrorResponseBody(
            errorCode = exception.errorCode,
            errorMessage = exception.errorMessage.ifEmpty { exception.message ?: "unknown error" },
        )
    }
}