package com.crazywedding.weddingbudgetplanner.authentication.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

@Entity
@Table(name = "AUTHORIZED_JWT")
class AuthorizedJwt(
    @Id
    @Column(length = 500)
    @Comment("JWT Access 토큰")
    val accessToken: String,
    @Column(length = 500)
    @Comment("Refresh 토큰 Hash")
    val encRefreshToken: String,
    @Column
    @Comment("Refresh 토큰 만료 시간")
    val expiredAt: LocalDateTime,
    @Column
    @Comment("토큰 발급 시간")
    val issuedAt: LocalDateTime,
)