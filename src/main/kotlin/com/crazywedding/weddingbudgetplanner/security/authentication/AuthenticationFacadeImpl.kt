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

        // 정상적인 인증객체를 들고있는 경우 (USER, ADMIN, ANONYMOUS)
        if (principal is Account) {
            return principal
        }

        throw IllegalStateException("")
    }
}