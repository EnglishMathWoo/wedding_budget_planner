package com.crazywedding.weddingbudgetplanner.authentication.dto.response

import com.crazywedding.weddingbudgetplanner.security.token.Jwt
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED

@Schema(description = "[어드민, 유저] 토큰 Response DTO")
data class TokenDto(
    @Schema(requiredMode = REQUIRED, description = "Access token")
    val accessToken: String,
    @Schema(requiredMode = REQUIRED, description = "Refresh token")
    val refreshToken: String
) {
    companion object {
        fun from(jwt: Jwt): TokenDto {
            return TokenDto(
                accessToken = jwt.accessToken,
                refreshToken = jwt.refreshToken
            )
        }
    }
}