package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class NotFoundResourceException(errorMessage: String) : BaseException(
    status = HttpStatus.NOT_FOUND,
    errorCode = ErrorCodeEnum.NOT_FOUND_RESOURCE.errorCode,
    errorMessage = errorMessage
)