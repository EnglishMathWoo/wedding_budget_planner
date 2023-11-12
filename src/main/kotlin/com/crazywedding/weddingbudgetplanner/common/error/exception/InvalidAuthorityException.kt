package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class InvalidAuthorityException(errorMessage: String) : BaseException(
    status = HttpStatus.CONFLICT,
    errorCode = ErrorCodeEnum.INVALID_AUTHORITY.errorCode,
    errorMessage = errorMessage
)