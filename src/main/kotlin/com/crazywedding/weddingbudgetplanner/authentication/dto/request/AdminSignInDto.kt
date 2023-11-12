package com.crazywedding.weddingbudgetplanner.authentication.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED

@Schema(description = "[어드민] 로그인 Request DTO")
data class AdminSignInDto(
    @Schema(requiredMode = REQUIRED, description = "아이디")
    val username: String,
    @Schema(requiredMode = REQUIRED, description = "비밀번호")
    val password: String
)