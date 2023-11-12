package com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper

import com.crazywedding.weddingbudgetplanner.authentication.dto.response.AdminDto
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED

@Schema(description = "[어드민] Wrapper 단일 어드민 Response DTO")
data class AdminWrapperDto(
    @Schema(requiredMode = REQUIRED, description = "어드민 상세정보")
    val admin: AdminDto
) {
    companion object {
        fun from(admin: AdminDto): AdminWrapperDto {
            return AdminWrapperDto(admin)
        }
    }
}