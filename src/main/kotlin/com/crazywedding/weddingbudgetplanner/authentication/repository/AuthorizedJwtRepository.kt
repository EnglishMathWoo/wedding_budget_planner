package com.crazywedding.weddingbudgetplanner.authentication.repository

import com.crazywedding.weddingbudgetplanner.authentication.entity.AuthorizedJwt
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorizedJwtRepository : JpaRepository<AuthorizedJwt, String>