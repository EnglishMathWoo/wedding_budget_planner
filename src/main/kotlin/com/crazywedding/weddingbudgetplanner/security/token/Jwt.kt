package com.crazywedding.weddingbudgetplanner.security.token

import com.crazywedding.weddingbudgetplanner.authentication.entity.AuthorizedJwt
data class Jwt(
    val accessToken: String,
    var refreshToken: String
) {

    companion object {
        fun from(authorizedJwt: AuthorizedJwt): Jwt {
            return Jwt(
                accessToken = authorizedJwt.accessToken,
                refreshToken = authorizedJwt.encRefreshToken
            )
        }
    }
}