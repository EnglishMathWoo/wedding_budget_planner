package com.crazywedding.weddingbudgetplanner.security.filter

import com.crazywedding.weddingbudgetplanner.security.token.UserRefreshTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class UserRefreshTokenFilter(
    private val tokenProvider: UserRefreshTokenProvider
) : OncePerRequestFilter() {

    private val matchers = arrayOf(
        AntPathRequestMatcher("/users/access-tokens/refresh"),
        AntPathRequestMatcher("/users/sign-out")
    )

    private fun getJwtFromHeader(request: HttpServletRequest): String? {
        val authorization = request.getHeader("Authorization")
        if (authorization.isNullOrEmpty() || !authorization.startsWith("Bearer ")) {
            return null
        }

        return authorization.substring(7)
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = getJwtFromHeader(request)
        if (jwt != null) {
            val authentication = tokenProvider.getAuthenticationFromToken(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return NegatedRequestMatcher(OrRequestMatcher(*matchers)).matches(request)
    }
}