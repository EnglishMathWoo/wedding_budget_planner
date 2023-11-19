package com.crazywedding.weddingbudgetplanner.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken

class AccountAuthenticationToken(
    private val account: Account,
    private val token: String?
) : AbstractAuthenticationToken(account.authorities) {

    init {
        isAuthenticated = token != null && token != ""
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