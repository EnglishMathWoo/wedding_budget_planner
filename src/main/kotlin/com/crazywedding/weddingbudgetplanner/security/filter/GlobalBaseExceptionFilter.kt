package com.crazywedding.weddingbudgetplanner.security.filter

import com.crazywedding.weddingbudgetplanner.common.error.exception.BaseException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import java.io.IOException

class GlobalBaseExceptionFilter(
    private val handlerExceptionResolver : HandlerExceptionResolver
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class, BaseException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }
}