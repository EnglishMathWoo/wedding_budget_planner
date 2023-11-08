package com.crazywedding.weddingbudgetplanner.security.token

import com.crazywedding.weddingbudgetplanner.authentication.entity.AuthorizedJwt
data class Jwt(
    val accessToken: String,
    var refreshToken: String
) {

    companion object {

        /**
         * (Server) DB 에 저장된 토큰 변환
         */
        fun from(authorizedJwt: AuthorizedJwt): Jwt {
            return Jwt(
                accessToken = authorizedJwt.accessToken,
                refreshToken = authorizedJwt.encRefreshToken
            )
        }
    }
    fun encryptRefreshToken(encToken: String) {
        this.refreshToken = encToken
    }
}