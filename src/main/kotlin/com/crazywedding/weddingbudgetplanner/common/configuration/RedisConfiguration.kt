package com.crazywedding.weddingbudgetplanner.common.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration
@EnableRedisRepositories
class RedisConfiguration {
    @Value("\${spring.data.redis.url}")
    lateinit var redisHost: String

    @Value("\${spring.data.redis.port}")
    var redisPort: Int = 6379

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(redisHost, redisPort)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory()
        template.stringSerializer = RedisSerializer.string()
        template.keySerializer = RedisSerializer.string()
        template.valueSerializer = RedisSerializer.json()
        template.hashKeySerializer = RedisSerializer.string()
        template.hashValueSerializer = RedisSerializer.json()
        return template
    }
}