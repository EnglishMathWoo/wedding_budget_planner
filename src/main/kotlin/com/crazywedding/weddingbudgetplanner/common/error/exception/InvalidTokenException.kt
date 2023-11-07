package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class InvalidTokenException(errorMessage: String) : BaseException(
    status = HttpStatus.UNAUTHORIZED,
    errorCode = ErrorCodeEnum.INVALID_TOKEN.errorCode,
    errorMessage = errorMessage
)