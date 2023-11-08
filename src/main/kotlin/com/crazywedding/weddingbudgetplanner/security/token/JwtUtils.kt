package com.crazywedding.weddingbudgetplanner.security.token

import jakarta.servlet.http.HttpServletRequest

class JwtUtils {

    companion object {
        private const val PRE_FIX = "Bearer"
        private const val AUTHORIZATION = "Authorization"
        private fun extractTokenFrom(value: String?): String? {
            return if (value != null && value.startsWith(PRE_FIX)) {
                value.substring(value.indexOf(PRE_FIX) + PRE_FIX.length + 1, value.length)
            } else {
                null
            }
        }

        fun extractTokenFrom(request: HttpServletRequest): String? {
            val value = request.getHeader(AUTHORIZATION)
            return extractTokenFrom(value)
        }
    }
}