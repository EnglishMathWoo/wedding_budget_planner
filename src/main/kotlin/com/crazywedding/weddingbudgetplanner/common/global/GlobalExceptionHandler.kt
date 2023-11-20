package com.crazywedding.weddingbudgetplanner.common.global

import com.crazywedding.weddingbudgetplanner.common.ErrorResponseBody
import com.crazywedding.weddingbudgetplanner.common.error.exception.*
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.IntStream

@RestControllerAdvice
class GlobalExceptionHandler {
    private fun traceCauseThrowable(
        throwable: Throwable,
        traceDepthLimit: Int = 20
    ): Set<Throwable> {
        val throwableSet: MutableSet<Throwable> = HashSet()
        var currentThrowable = throwable

        for (i in IntStream.range(1, traceDepthLimit).toArray()) {
            throwableSet.add(currentThrowable)
            val throwableCause = currentThrowable.cause
            if (throwableCause == null || throwableCause.javaClass == currentThrowable.javaClass) {
                break
            }
            currentThrowable = throwableCause
        }
        return throwableSet
    }

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadableException(
        exception: HttpMessageNotReadableException
    ): ResponseEntity<ErrorResponseBody?>? {
        logger.error("handleHttpMessageNotReadableException", exception)
        val throwableSet = traceCauseThrowable(exception)
        val irpException = throwableSet.stream()
            .filter { t: Throwable -> t.javaClass == InvalidRequestParameterException::class.java }
            .map { e: Throwable? -> e as InvalidRequestParameterException? }
            .findFirst()
            .orElse(null)

        if (irpException != null) {
            val responseBody = ErrorResponseBody.of(irpException)
            return ResponseEntity(responseBody, irpException.status)
        }

        val baseException: BaseException = InvalidRequestParameterException("입력한 파라미터가 올바르지 않습니다.")
        val responseBody = ErrorResponseBody.of(baseException)
        return ResponseEntity(responseBody, baseException.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponseBody?>? {
        logger.error("handleMethodArgumentNotValidException", exception)
        var errorMessage = "입력한 파라미터가 올바르지 않습니다."
        val fieldErrors = exception.fieldErrors
        if (fieldErrors.isNotEmpty()) {
            errorMessage = fieldErrors[0].defaultMessage.toString()
        }
        val baseException: BaseException = InvalidRequestParameterException(errorMessage)
        val responseBody = ErrorResponseBody.of(baseException)
        return ResponseEntity(responseBody, baseException.status)
    }

    @ExceptionHandler(AuthenticationException::class)
    protected fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<ErrorResponseBody?>? {
        logger.error("handleAuthenticationException", e)
        val baseException = InvalidAuthorityException(e.localizedMessage)
        return ResponseEntity(ErrorResponseBody.of(baseException), baseException.status)
    }

    @ExceptionHandler(AccessDeniedException::class)
    protected fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<ErrorResponseBody?>? {
        logger.error("handleAccessDeniedException", e)
        val baseException = AccessDeniedResourceException(e.localizedMessage)
        return ResponseEntity(ErrorResponseBody.of(baseException), baseException.status)
    }

    @ExceptionHandler(JwtException::class)
    protected fun handleJwtException(e: JwtException): ResponseEntity<ErrorResponseBody?>? {
        logger.error("handleJwtException ", e)
        val baseException = when (e) {
            is ExpiredJwtException -> ExpiredTokenException("토큰이 만료 되었습니다.")
            else -> InvalidAuthorityException(e.localizedMessage)
        }

        return ResponseEntity(ErrorResponseBody.of(baseException), baseException.status)
    }

    @ExceptionHandler(BaseException::class)
    protected fun <T : BaseException> handleBaseException(e: T): ResponseEntity<ErrorResponseBody?>? {
        logger.error("handleBaseException", e)
        val responseBody = ErrorResponseBody.of(e)
        return ResponseEntity(responseBody, e.status)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(exception: Exception): ResponseEntity<ErrorResponseBody?>? {
        logger.error("handleException", exception)
        val baseException: BaseException = InternalServerException(exception.message!!)
        val responseBody = ErrorResponseBody.of(baseException)
        return ResponseEntity(responseBody, baseException.status)
    }
}