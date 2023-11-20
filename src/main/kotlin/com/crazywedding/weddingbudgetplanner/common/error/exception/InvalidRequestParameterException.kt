package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class InvalidRequestParameterException(errorMessage: String) : BaseException(
    status = HttpStatus.UNPROCESSABLE_ENTITY,
    errorCode = ErrorCodeEnum.INVALID_REQUEST_PARAMETER.errorCode,
    errorMessage = errorMessage
) {
}