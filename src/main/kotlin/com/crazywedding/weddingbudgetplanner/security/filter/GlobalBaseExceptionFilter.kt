package com.crazywedding.weddingbudgetplanner.security.filter

import com.crazywedding.weddingbudgetplanner.common.error.exception.BaseException
import com.crazywedding.weddingbudgetplanner.common.error.exception.InternalServerException
import com.fasterxml.jackson.databind.ObjectMapper
import com.crazywedding.weddingbudgetplanner.common.dto.ErrorResponseBody
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class GlobalBaseExceptionFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class, BaseException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BaseException) {
            setErrorResponse(response, e)
        } catch (e: Exception) {
            e.printStackTrace()
            val baseException: BaseException = InternalServerException(e.message!!)
            setErrorResponse(response, baseException)
        }
    }

    private fun setErrorResponse(
        response: HttpServletResponse,
        baseException: BaseException,
    ) {
        val objectMapper = ObjectMapper()
        val responseBody = ErrorResponseBody(baseException)

        response.status = baseException.status.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        try {
            response.writer.write(objectMapper.writeValueAsString(responseBody))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}