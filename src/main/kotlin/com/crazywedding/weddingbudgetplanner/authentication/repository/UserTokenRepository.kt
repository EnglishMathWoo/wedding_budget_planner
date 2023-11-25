package com.crazywedding.weddingbudgetplanner.authentication.repository

import com.crazywedding.weddingbudgetplanner.authentication.entity.UserToken
import org.springframework.data.jpa.repository.JpaRepository

interface UserTokenRepository {
    fun save(userToken: UserToken)
    fun findByRefreshToken(refreshToken: String): UserToken?
    fun delete(userToken: UserToken)
}