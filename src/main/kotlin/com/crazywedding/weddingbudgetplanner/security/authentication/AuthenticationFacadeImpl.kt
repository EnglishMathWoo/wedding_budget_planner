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

        // custom security filter 를 통해 들어오지 않았으나 익명 인증객체를 들고있는 경우
        if (isAnonymous(principal)) {
            return Account.anonymous()
        }
        // 정상적인 인증객체를 들고있는 경우 (USER, ADMIN, ANONYMOUS)
        if (principal is Account) {
            return principal
        }
        // 해당 exception 이 실행되면 약속된 Principal 객체가 아니었을때 실행됩니다.
        throw IllegalStateException("")
    }

    private fun getAuthenticationOrThrowEx(): Authentication {
        // security filter chain 을 통해 들어온 요청이 아닐 시 null 입니다.
        return SecurityContextHolder.getContext().authentication
            ?: throw AccessDeniedException("허가되지 않은 접근 입니다.")
    }

    private fun isAnonymous(principal: Any): Boolean {
        // Test 코드 실행시 익명 인증객체의 principal 를 위함
        // @WithAnonymousUser
        return principal == "anonymous" || principal == "anonymousUser"
    }
}