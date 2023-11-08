package com.crazywedding.weddingbudgetplanner.security.handler


import com.crazywedding.weddingbudgetplanner.common.error.exception.InvalidTokenException
import com.crazywedding.weddingbudgetplanner.security.token.JwtProvider
import com.crazywedding.weddingbudgetplanner.security.token.JwtUtils
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication

class JwtAuthenticateHandler(
    private val jwtProvider: JwtProvider
) : AuthenticateHandler {
    override fun authenticate(request: HttpServletRequest): Authentication {
        val jwt = JwtUtils.extractTokenFrom(request)
        return createAuthentication(jwt)
    }

    private fun createAuthentication(token: String?): Authentication {
        if (token == null) throw InvalidTokenException("Empty Authorization Error")
        return jwtProvider.authenticate(token)
    }
}