package com.crazywedding.weddingbudgetplanner.authentication.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDateTime

@RedisHash(value = "user_tokens")
class UserToken(
    @Id
    val refreshToken: String,
    var accessToken: String,
    val userId: Long,
)