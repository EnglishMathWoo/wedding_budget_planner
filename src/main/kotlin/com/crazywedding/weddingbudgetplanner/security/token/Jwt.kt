package com.crazywedding.weddingbudgetplanner.security.token

import com.crazywedding.weddingbudgetplanner.authentication.entity.UserToken

data class Jwt(
    val accessToken: String,
    var refreshToken: String
) {

    companion object {
        fun from(authorizedJwt: UserToken): Jwt {
            return Jwt(
                accessToken = authorizedJwt.accessToken,
                refreshToken = authorizedJwt.refreshToken
            )
        }
    }
}