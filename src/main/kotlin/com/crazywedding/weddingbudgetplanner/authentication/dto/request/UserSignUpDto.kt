package com.crazywedding.weddingbudgetplanner.authentication.dto.request

import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import com.crazywedding.weddingbudgetplanner.common.validator.PhoneFormat
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED
import jakarta.validation.constraints.Email

@Schema(description = "[유저] 회원가입 Request DTO")
data class UserSignUpDto(
    @Schema(requiredMode = REQUIRED, description = "로그인 아이디")
    val username: String,
    @Schema(requiredMode = REQUIRED, description = "비밀번호")
    val password: String,
    @Schema(requiredMode = REQUIRED, description = "이름")
    val name: String,
    @field:PhoneFormat
    @Schema(requiredMode = REQUIRED, description = "전화번호")
    val phone: String,
    @field:Email
    @Schema(requiredMode = REQUIRED, description = "이메일", nullable = true)
    val email: String?,
) {
    fun toEntity(encryptedPassword: String): User {
        return User(
            username = username,
            encryptedPassword = encryptedPassword,
            name = name,
            email = email,
            phone = phone,
        )
    }
}