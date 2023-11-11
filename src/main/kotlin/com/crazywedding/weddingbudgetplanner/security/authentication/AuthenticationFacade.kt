package com.crazywedding.weddingbudgetplanner.security.authentication

import org.springframework.security.core.Authentication

interface AuthenticationFacade {
    fun getAuthentication(): Authentication
    fun getPrincipal(): Account
}