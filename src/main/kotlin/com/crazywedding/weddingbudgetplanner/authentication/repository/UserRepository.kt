package com.crazywedding.weddingbudgetplanner.authentication.repository

import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}