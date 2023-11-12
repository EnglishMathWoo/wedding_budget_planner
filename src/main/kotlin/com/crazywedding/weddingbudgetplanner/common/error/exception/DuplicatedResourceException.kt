package com.crazywedding.weddingbudgetplanner.common.error.exception

import com.crazywedding.weddingbudgetplanner.common.error.ErrorCodeEnum
import org.springframework.http.HttpStatus

class DuplicatedResourceException(errorMessage: String) : BaseException(
    status = HttpStatus.CONFLICT,
    errorCode = ErrorCodeEnum.DUPLICATED_RESOURCE.errorCode,
    errorMessage = errorMessage
)