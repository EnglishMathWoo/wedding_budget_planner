package com.crazywedding.weddingbudgetplanner.security.principal

class UserPrincipal(
    private val userId: Long,
    private val userName: String
) : Principal {
    override fun getId(): Long {
        return userId
    }

    override fun getUsername(): String {
        return userName
    }
}