package com.crazywedding.weddingbudgetplanner.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * @param account 기본 계정 정보
 * @param token jwt accessToken
 */
class AccountAuthenticationToken(
    private val account: Account,
    private val token: String?
) : AbstractAuthenticationToken(account.authorities) {
    init {
        // 현재 버전에서는 Detail 정보를 사용하지 않습니다.
        this.details = null
        // (토큰 인증을 하지 않은) Anonymous 객체는 인증을 허가하지 않습니다.
        this.isAuthenticated = token != null && token != ""
    }

    companion object {
        fun anonymousToken(): AccountAuthenticationToken {
            return AccountAuthenticationToken(Account.anonymous(), "")
        }
    }

    override fun getCredentials(): String? {
        return token
    }

    override fun getPrincipal(): Account {
        return account
    }
}