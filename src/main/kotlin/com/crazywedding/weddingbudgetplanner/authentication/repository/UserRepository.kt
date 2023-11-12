package com.crazywedding.weddingbudgetplanner.authentication.repository

import com.crazywedding.weddingbudgetplanner.authentication.entity.Admin
import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun existsByPhone(phone: String): Boolean
    fun findByPhone(phone: String): User?
    fun existsByEmail(phone: String): Boolean
    fun findByEmail(email: String): User?

    @Query("SELECT c.id FROM Ceo c WHERE c.user.id = :id")
    fun findCeoIdById(@Param("id") id: Long): Optional<Long>
}