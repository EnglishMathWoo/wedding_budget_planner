package com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper

import com.crazywedding.weddingbudgetplanner.authentication.dto.response.UserDto
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[유저] Wrapper 단일 어드민 Response DTO")
data class UserWrapperDto(
    @Schema(description = "어드민 상세정보")
    val user: UserDto
) {
    companion object {
        fun from(user: UserDto): UserWrapperDto {
            return UserWrapperDto(user)
        }
    }
}