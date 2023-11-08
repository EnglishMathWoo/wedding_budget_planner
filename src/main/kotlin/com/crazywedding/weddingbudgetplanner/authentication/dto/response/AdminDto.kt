package com.crazywedding.weddingbudgetplanner.authentication.dto.response

import com.crazywedding.weddingbudgetplanner.authentication.entity.Admin
import com.crazywedding.weddingbudgetplanner.common.error.exception.InternalServerException
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED
import java.time.LocalDateTime

@Schema(description = "[어드민] 어드민 Response DTO")
data class AdminDto(
    @Schema(requiredMode = REQUIRED, description = "id(pk)")
    val id: Long,
    @Schema(requiredMode = REQUIRED, description = "author Id")
    val authorId: Long,
    @Schema(requiredMode = REQUIRED, description = "아이디")
    val username: String,
    @Schema(requiredMode = REQUIRED, description = "이름", nullable = true)
    val name: String?,
    @Schema(requiredMode = REQUIRED, description = "휴대폰번호", nullable = true)
    val phone: String?,
    @Schema(requiredMode = REQUIRED, description = "이메일", nullable = true)
    val email: String?,
    @Schema(requiredMode = REQUIRED, description = "내선번호", nullable = true)
    val extensionNumber: String?,
    @Schema(requiredMode = REQUIRED, description = "내선번호(4자리)", nullable = true)
    val extensionNumberSuffix: String?,
    @Schema(requiredMode = REQUIRED, description = "생성일")
    val createdAt: LocalDateTime,
    @Schema(requiredMode = REQUIRED, description = "수정일")
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(entity: Admin?): AdminDto? {
            if (entity == null) return null

            return AdminDto(
                id = entity.id ?: throw InternalServerException("entity는 id값이 존재해야한다."),
                authorId = entity.author?.id!!,
                username = entity.username,
                name = entity.name,
                phone = entity.phone,
                email = entity.email,
                extensionNumber = entity.extensionNumber,
                extensionNumberSuffix = entity.extensionNumberSuffix,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}