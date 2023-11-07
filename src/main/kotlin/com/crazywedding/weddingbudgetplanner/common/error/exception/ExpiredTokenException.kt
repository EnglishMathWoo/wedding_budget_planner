package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class ExpiredTokenException(errorMessage: String) : BaseException(
    status = HttpStatus.UNAUTHORIZED,
    errorCode = ErrorCodeEnum.EXPIRED_TOKEN.errorCode,
    errorMessage = errorMessage
)