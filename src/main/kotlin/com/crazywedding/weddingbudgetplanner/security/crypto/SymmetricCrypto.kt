package com.crazywedding.weddingbudgetplanner.security.crypto

interface SymmetricCrypto {
    fun encrypt(plainText: String): String
    fun decrypt(cipherText: String?): String
}