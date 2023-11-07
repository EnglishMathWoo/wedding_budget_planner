package com.crazywedding.weddingbudgetplanner.security.token

import com.crazywedding.weddingbudgetplanner.security.token.TokenAudienceEnum.USER_REFRESH_TOKEN
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class UserRefreshTokenProvider : BaseTokenProvider() {
    @Value("\${authentication.issuer}")
    private lateinit var issuer: String

    @Value("\${authentication.user.refresh-token.secret-key}")
    private lateinit var tokenSecretKey: String

    @Value("\${authentication.user.refresh-token.expired-seconds}")
    private lateinit var tokenExpiredSeconds: String

    override fun getExpiredSeconds(): Long {
        return tokenExpiredSeconds.toLong()
    }

    fun generateToken(id: Long, subject: TokenSubjectEnum, username: String): String {
        return super.generateToken(
            issuer = issuer,
            secretKey = tokenSecretKey,
            expiredSeconds = getExpiredSeconds(),
            id = id,
            audience = USER_REFRESH_TOKEN.content,
            subject = subject.content,
            username = username
        )
    }

    fun getAuthenticationFromToken(token: String): Authentication {
        return super.getAuthenticationFromToken(
            secretKey = tokenSecretKey,
            token = token,
            validAudience = USER_REFRESH_TOKEN.content
        )
    }

    fun getAuthenticationIdFromToken(token: String): Long {
        return super.getAuthenticationIdFromToken(
            secretKey = tokenSecretKey,
            token = token,
            validAudience = USER_REFRESH_TOKEN.content
        )
    }
}
