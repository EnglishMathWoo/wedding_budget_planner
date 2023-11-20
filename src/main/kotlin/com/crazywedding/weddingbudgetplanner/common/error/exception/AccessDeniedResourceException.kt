package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class AccessDeniedResourceException(errorMessage: String) : BaseException(
    status = HttpStatus.FORBIDDEN,
    errorCode = ErrorCodeEnum.ACCESS_DENIED_RESOURCE.errorCode,
    errorMessage = errorMessage
) {
}