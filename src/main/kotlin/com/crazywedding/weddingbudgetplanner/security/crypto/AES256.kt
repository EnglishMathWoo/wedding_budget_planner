package com.crazywedding.weddingbudgetplanner.security.crypto

import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AES256(key: String) : SymmetricCrypto {

    companion object {
        private const val ALG = "AES/CBC/PKCS5Padding"
    }
    private val iv: String

    init {
        iv = key.substring(0, 16)
    }
    override fun encrypt(plainText: String): String {
        return try {
            val cipher = Cipher.getInstance(ALG)
            val keySpec = SecretKeySpec(iv.toByteArray(), "AES")
            val ivParamSpec = IvParameterSpec(iv.toByteArray())
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec)
            val encrypted = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            Base64.getEncoder().encodeToString(encrypted)
        } catch (ex: Exception) {
            throw CryptoException(ex)
        }
    }

    override fun decrypt(cipherText: String?): String {
        try {
            val cipher = Cipher.getInstance(ALG)
            val keySpec = SecretKeySpec(iv.toByteArray(), "AES")
            val ivParamSpec = IvParameterSpec(iv.toByteArray())
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec)
            val decodedBytes = Base64.getDecoder().decode(cipherText)
            val decrypted = cipher.doFinal(decodedBytes)
            return String(decrypted, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            throw CryptoException(ex)
        }
    }
}