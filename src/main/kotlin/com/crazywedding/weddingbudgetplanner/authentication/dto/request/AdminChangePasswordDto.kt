package com.crazywedding.weddingbudgetplanner.authentication.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED

@Schema(description = "[어드민] 비밀번호 변경 Request DTO")
data class AdminChangePasswordDto(
    @Schema(requiredMode = REQUIRED, description = "현재 비밀번호")
    val currentPassword: String,
    @Schema(requiredMode = REQUIRED, description = "변경하려는 비밀번호")
    val newPassword: String
)