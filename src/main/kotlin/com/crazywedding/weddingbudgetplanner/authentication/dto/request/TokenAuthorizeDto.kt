package com.crazywedding.weddingbudgetplanner.authentication.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "토큰 인증 DTO")
data class TokenAuthorizeDto(
    @field:NotBlank
    @Schema(description = "Refresh 토큰")
    val refreshToken: String,
    @field:NotBlank
    @Schema(description = "Access 토큰")
    val accessToken: String,
)