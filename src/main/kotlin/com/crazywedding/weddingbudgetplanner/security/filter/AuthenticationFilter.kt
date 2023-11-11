package com.crazywedding.weddingbudgetplanner.security.filter

import com.crazywedding.weddingbudgetplanner.security.handler.AuthenticateHandler
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class AuthenticationFilter(
    private val matcher: RequestMatcher,
    private val authenticateHandler: AuthenticateHandler
) : OncePerRequestFilter() {

    private fun attemptToAuthenticate(request: HttpServletRequest) {
        // if auth fail then return 'Anonymous Authentication'
        val authentication = authenticateHandler.authenticate(request)
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        attemptToAuthenticate(request)
        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return NegatedRequestMatcher(matcher).matches(request)
    }
}