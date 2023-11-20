package com.crazywedding.weddingbudgetplanner.security.authentication

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationFacadeImpl : AuthenticationFacade {
    override fun getAuthentication(): Authentication {
        return SecurityContextHolder.getContext().authentication
    }

    override fun getPrincipal(): Account {
        val authentication = getAuthentication()
        val principal = authentication.principal

        if (principal is Account) {
            return principal
        }

        throw IllegalStateException("정상적으로 인증되지 않았습니다.")
    }
}