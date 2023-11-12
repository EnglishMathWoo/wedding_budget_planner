package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class IncorrectPasswordException(errorMessage: String) : BaseException(
    status = HttpStatus.UNAUTHORIZED,
    errorCode = ErrorCodeEnum.INCORRECT_PASSWORD.errorCode,
    errorMessage = errorMessage
)