package com.crazywedding.weddingbudgetplanner.security.token

enum class TokenAudienceEnum(
    val content: String
) {
    USER_ACCESS_TOKEN("userAccessToken"),
    USER_REFRESH_TOKEN("userRefreshToken")
}