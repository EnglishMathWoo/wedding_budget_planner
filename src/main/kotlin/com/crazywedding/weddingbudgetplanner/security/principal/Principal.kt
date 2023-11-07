package com.crazywedding.weddingbudgetplanner.security.principal

interface Principal {

    fun getId(): Long

    fun getUsername(): String
}