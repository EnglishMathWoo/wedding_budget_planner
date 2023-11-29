package com.crazywedding.weddingbudgetplanner.authentication.repository.impl

import com.crazywedding.weddingbudgetplanner.authentication.entity.UserToken
import com.crazywedding.weddingbudgetplanner.authentication.repository.UserTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class UserTokenRepositoryImpl(
    val redisTemplate: RedisTemplate<String, Any>
) : UserTokenRepository {
    @Value("\${authentication.user.refresh-token.expired-seconds}")
    private lateinit var tokenExpiredSeconds: String

    override fun save(userToken: UserToken) {
        val key = innerRedisKey(userToken.refreshToken)
        val opsHash = redisTemplate.opsForHash<String, Any>()
        val map = innerUserTokenToMap(userToken)
        val tokenExistence = opsHash.entries(key).isNotEmpty()

        opsHash.putAll(key, map)
        if (!tokenExistence) {
            redisTemplate.expire(key, tokenExpiredSeconds.toLong(), TimeUnit.SECONDS)
        }
     }

    override fun findByRefreshToken(refreshToken: String): UserToken? {
        val key = innerRedisKey(refreshToken)
        val opsHash = redisTemplate.opsForHash<String, Any>()
        val map = opsHash.entries(key)

        if (map.isEmpty()) return null
        return innerMapToUserToken(map)
    }

    override fun delete(userToken: UserToken) {
        val key = innerRedisKey(userToken.refreshToken)
        val opsHash = redisTemplate.opsForHash<String, Any>()
        val keyNames = innerUserTokenKeyNames()
        opsHash.delete(key, *keyNames)
        return
    }

    fun innerRedisKey(refreshToken: String) = "user_tokens:$refreshToken"

    fun innerUserTokenKeyNames() = arrayOf("accessToken", "refreshToken", "userId")

    fun innerUserTokenToMap(userToken: UserToken) = mapOf(
        "refreshToken" to userToken.refreshToken,
        "accessToken" to userToken.accessToken,
        "userId" to userToken.userId
    )

    fun innerMapToUserToken(map: Map<String, Any>) = UserToken(
        accessToken = map["accessToken"].toString(),
        refreshToken = map["refreshToken"].toString(),
        userId = map["userId"].toString().toLong()
    )
}