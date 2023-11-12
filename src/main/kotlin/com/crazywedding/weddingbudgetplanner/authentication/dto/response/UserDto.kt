package com.crazywedding.weddingbudgetplanner.authentication.dto.response

import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import com.crazywedding.weddingbudgetplanner.common.error.exception.InternalServerException
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

@Schema(description = "유저")
data class UserDto(
    @Schema(description = "id")
    val id: Long,
    @Schema(description = "author id")
    val authorId: Long,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "아이디")
    val username: String,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "이름", nullable = true)
    val name: String?,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "휴대폰번호", nullable = true)
    val phone: String?,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "이메일", nullable = true)
    val email: String?,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "생성일")
    val createdAt: LocalDateTime,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "수정일")
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(entity: User): UserDto {
            return UserDto(
                id = entity.id ?: throw InternalServerException("entity는 id값이 존재해야한다."),
                authorId = entity.author?.id!!,
                username = entity.username,
                name = entity.name,
                phone = entity.phone,
                email = entity.email,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}