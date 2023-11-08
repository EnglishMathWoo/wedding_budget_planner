package com.crazywedding.weddingbudgetplanner.authentication.dto.response

import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Comment

@Schema(description = "유저")
data class UserDto(
    @Schema(description = "id")
    val id: Long,
    @Schema(description = "author id")
    val authorId: Long,
    @Comment(value = "휴대폰번호")
    var phone: String,
    @Comment(value = "이메일")
    var email: String,
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id!!,
                phone = user.phone,
                email = user.email,
                authorId = user.author?.id!!,
            )
        }
    }
}