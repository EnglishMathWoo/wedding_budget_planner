package com.crazywedding.weddingbudgetplanner.security.token

enum class TokenSubjectEnum(
    val content: String
) {
    SIGN_IN("signIn"),
    REFRESH_ACCESS_TOKEN("refreshAccessToken")
}