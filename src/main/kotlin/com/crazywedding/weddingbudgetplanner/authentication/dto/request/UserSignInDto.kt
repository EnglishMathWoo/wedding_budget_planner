package com.crazywedding.weddingbudgetplanner.authentication.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED

@Schema(description = "[유저] 로그인 Request DTO")
data class UserSignInDto(
    @Schema(requiredMode = REQUIRED, description = "아이디")
    val username: String,
    @Schema(requiredMode = REQUIRED, description = "비밀번호")
    val password: String
)