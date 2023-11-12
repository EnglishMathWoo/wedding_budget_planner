package com.crazywedding.weddingbudgetplanner.authentication.repository

import com.crazywedding.weddingbudgetplanner.authentication.entity.Admin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AdminRepository : JpaRepository<Admin, Long> {
    fun findAdminByUsername(username: String): Admin?
    fun existsByUsername(username: String): Boolean

    @Query(value = "SELECT a from Admin a WHERE a.author.id IN :authorIds ORDER BY a.createdAt DESC")
    fun findAllByAuthorIds(@Param("authorIds") authorIds: List<Long>): List<Admin>
}