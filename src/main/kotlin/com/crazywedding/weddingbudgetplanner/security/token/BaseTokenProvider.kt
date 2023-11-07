package com.crazywedding.weddingbudgetplanner.security.token

import com.crazywedding.weddingbudgetplanner.common.error.exception.ExpiredTokenException
import com.crazywedding.weddingbudgetplanner.common.error.exception.InternalServerException
import com.crazywedding.weddingbudgetplanner.common.error.exception.InvalidTokenException
import com.crazywedding.weddingbudgetplanner.security.principal.Principal
import com.crazywedding.weddingbudgetplanner.security.principal.UserPrincipal
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.nio.charset.StandardCharsets
import java.util.*

abstract class BaseTokenProvider {

    abstract fun getExpiredSeconds(): Long

    protected fun generateToken(
        issuer: String,
        secretKey: String,
        expiredSeconds: Long,
        id: Long,
        audience: String,
        subject: String,
        username: String
    ): String {
        try {
            val now = Date()
            val expiredAt = Date(now.time + (expiredSeconds * 1000))
            val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

            val claims = Jwts.claims()

            claims.setSubject(subject)
                .setId(id.toString())
                .setExpiration(expiredAt)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setAudience(audience)

            claims["username"] = username

            return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
        } catch (e: Exception) {
            throw InternalServerException("토큰 생성에 실패했습니다.")
        }
    }

    protected fun getAuthenticationFromToken(
        secretKey: String,
        token: String,
        validAudience: String
    ): Authentication {
        val claimBody = innerGetClaimBodyFromJwt(secretKey, token)

        val tokenId = try {
            claimBody.id.toLong()
        } catch (e: Exception) {
            throw InvalidTokenException("토큰 id 정보가 올바르지 않습니다.(not long type)")
        }

        val username = try {
            claimBody["username"].toString()
        } catch (e: Exception) {
            throw InvalidTokenException("토큰 유저네임 정보가 올바르지 않습니다.")
        }

        if (tokenId < 1) {
            throw InvalidTokenException("토큰 id 정보가 올바르지 않습니다.")
        }

        if (claimBody.audience != validAudience) {
            throw InvalidTokenException("토큰 audience 정보가 올바르지 않습니다.")
        }

        val principal = UserPrincipal(
            userId = tokenId,
            userName = username
        )

        return UsernamePasswordAuthenticationToken(
            principal,
            ""
        )
    }

    protected fun getAuthenticationIdFromToken(
        secretKey: String,
        token: String,
        validAudience: String
    ): Long {
        val authentication = getAuthenticationFromToken(secretKey, token, validAudience)
        val principal = authentication.principal as Principal

        return principal.getId()
    }

    protected fun getAuthenticationUsernameFromToken(
        secretKey: String,
        token: String,
        validAudience: String
    ): String {
        val authentication = getAuthenticationFromToken(secretKey, token, validAudience)
        val principal = authentication.principal as Principal

        return principal.getUsername()
    }

    private fun innerGetClaimBodyFromJwt(
        secretKey: String,
        token: String,
    ): Claims {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
        val parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()

        val claimBody = try {
            parser.parseClaimsJws(token).body
        } catch (e: Exception) {
            when (e) {
                is UnsupportedJwtException -> throw InvalidTokenException("지원하지 않는 argument를 사용합니다.")
                is MalformedJwtException -> throw InvalidTokenException("토큰 문자열이 유효하지 않습니다.")
                is SignatureException -> throw InvalidTokenException("signature 값이 유효하지 않습니다.")
                is IllegalArgumentException -> throw InvalidTokenException("토큰 문자열이 유효하지 않습니다.(공백)")
                is ExpiredJwtException -> throw ExpiredTokenException("토큰이 만료되었습니다.")
                else -> throw InternalServerException("토큰 인증 과정에서 오류가 발생했습니다.")
            }
        }

        return claimBody
    }
}