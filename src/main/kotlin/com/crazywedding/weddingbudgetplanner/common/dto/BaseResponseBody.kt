package com.crazywedding.weddingbudgetplanner.common.dto

import com.crazywedding.weddingbudgetplanner.common.error.exception.BaseException


abstract class BaseResponseBody(val result: Any?, exception: BaseException?) {
    val error: ErrorDetail?

    init {
        this.error = if (exception != null) ErrorDetail(exception) else null
    }

    inner class ErrorDetail(exception: BaseException) {
        private val errorCode: String
        private val errorMessage: String?

        init {
            this.errorCode = exception.errorCode
            this.errorMessage = exception.errorMessage.ifEmpty { exception.message }
        }
    }
}