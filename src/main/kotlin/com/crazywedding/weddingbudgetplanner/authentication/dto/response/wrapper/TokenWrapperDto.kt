package com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper

import com.crazywedding.weddingbudgetplanner.authentication.dto.response.TokenDto
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[공통] 토큰 Response DTO")
data class TokenWrapperDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "토큰")
    val token: TokenDto
) {
    companion object {
        fun from(token: TokenDto): TokenWrapperDto {
            return TokenWrapperDto(token)
        }
    }
}