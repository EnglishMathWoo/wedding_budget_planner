package com.crazywedding.weddingbudgetplanner.authentication.entity

import com.crazywedding.weddingbudgetplanner.authentication.entity.enum.AccountTypeEnum
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

@Entity
@Table(name = "AUTHORIZED_JWT")
class AuthorizedJwt(
    @Id
    @Column(length = 500)
    @Comment("Access Token")
    val accessToken: String,

    @Column(length = 500)
    @Comment("암호화된 Refresh Token")
    val encRefreshToken: String,

    @Column
    @Comment("Refresh Token 만료 시간")
    val expiredAt: LocalDateTime,

    @Column
    @Comment("Token 발급 시간")
    val issuedAt: LocalDateTime,

    @Column
    @Enumerated(EnumType.STRING)
    val accountType: AccountTypeEnum,

    @Column
    val accountId: Long
)