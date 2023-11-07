package com.crazywedding.weddingbudgetplanner.common.dto

import com.crazywedding.weddingbudgetplanner.common.error.exception.BaseException

class ErrorResponseBody(exception: BaseException) : BaseResponseBody(result = null, exception = exception)