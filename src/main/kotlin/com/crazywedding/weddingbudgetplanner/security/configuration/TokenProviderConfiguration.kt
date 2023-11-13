package com.crazywedding.weddingbudgetplanner.security.configuration

import com.crazywedding.weddingbudgetplanner.security.token.JwtProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenProviderConfiguration {

    @Bean("userJwtProvider")
    fun userJwtProvider(
        @Value("\${authentication.issuer}")
        iss: String,
        @Value("\${authentication.user.access-token.secret-key}")
        accessTokenSecretKey: String,
        @Value("\${authentication.user.access-token.expired-seconds}")
        accessTokenExpTime: Long,
    ): JwtProvider {
        return JwtProvider(
            iss,
            accessTokenExpTime,
            accessTokenSecretKey
        )
    }

    @Bean("adminJwtProvider")
    fun adminJwtProvider(
        @Value("\${authentication.issuer}")
        iss: String,
        @Value("\${authentication.admin.access-token.secret-key}")
        accessTokenSecretKey: String,
        @Value("\${authentication.admin.access-token.expired-seconds}")
        accessTokenExpTime: Long
    ): JwtProvider {

        return JwtProvider(
            iss,
            accessTokenExpTime,
            accessTokenSecretKey
        )
    }
}