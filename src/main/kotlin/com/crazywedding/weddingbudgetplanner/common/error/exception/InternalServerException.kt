package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class InternalServerException(errorMessage: String) : BaseException(
    status = HttpStatus.INTERNAL_SERVER_ERROR,
    errorCode = ErrorCodeEnum.INTERNAL_SERVER.errorCode,
    errorMessage = errorMessage
)