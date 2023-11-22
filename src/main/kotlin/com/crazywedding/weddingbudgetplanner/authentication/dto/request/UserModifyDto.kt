package com.crazywedding.weddingbudgetplanner.authentication.dto.request

import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import com.crazywedding.weddingbudgetplanner.common.validator.PhoneFormat
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED
import jakarta.validation.constraints.Email

@Schema(description = "[유저] 수정 Request DTO")
data class UserModifyDto(
    @Schema(requiredMode = NOT_REQUIRED, description = "이름")
    val name: String?,
    @field:PhoneFormat
    @Schema(requiredMode = NOT_REQUIRED, description = "휴대폰번호")
    val phone: String?,
    @field:Email
    @Schema(requiredMode = NOT_REQUIRED, description = "이메일")
    val email: String?
) {
    fun modifyEntity(user: User): User {
        name?.let { user.name = it }
        phone?.let { user.phone = it }
        email?.let { user.email = it }
        return user
    }
}