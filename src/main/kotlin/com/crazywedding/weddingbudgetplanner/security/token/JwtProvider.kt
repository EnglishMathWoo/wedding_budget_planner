package com.crazywedding.weddingbudgetplanner.security.token

import com.crazywedding.weddingbudgetplanner.security.authentication.Account
import com.crazywedding.weddingbudgetplanner.security.authentication.AccountAuthenticationToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.authority.AuthorityUtils
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey

class JwtProvider(
    private val iss: String,
    private val accessTokenExpTime: Long,
    private val accessTokenSecretKey: String,
) : AuthTokenProvider<Jwt, Account> {

    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(accessTokenSecretKey.toByteArray(StandardCharsets.UTF_8))
    private val tokenParser: JwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build()

    companion object {
        const val NAME_KEY = "name"
        const val AUTHORITIES_KEY = "auths"
        const val AUTHOR_ID_KEY = "author_id"
    }

    override fun create(principal: Account, issuedAt: LocalDateTime): Jwt {
        return Jwt(
            accessToken = createNewToken(principal, issuedAt),
            refreshToken = UUID.randomUUID().toString() //
        )
    }

    override fun authenticate(token: String): AccountAuthenticationToken {
        return AccountAuthenticationToken(
            tokenToAccount(token),
            token
        )
    }

    override fun validate(token: String) {
        tokenParser.parseClaimsJws(token)
    }

    /**
     * 토큰 => Account
     * @exception InvalidAuthorityException 존재 하지 않는 Account 호출시
     */
    private fun tokenToAccount(token: String): Account {
        val claims = this.tokenToClaims(token)
        return claimsToAccount(claims)
    }

    private fun claimsToAccount(claims: Claims): Account {
        val userId = claims.subject!!.toLong()
        val authorId = claims[AUTHOR_ID_KEY].toString().toLong()
        val authorities = claims[AUTHORITIES_KEY].toString()
        val email = claims[NAME_KEY].toString()
        return Account(
            id = userId,
            name = email,
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities),
            authorId = authorId
        )
    }

    /**
     * 토큰을 claims 로 변경 (Token Provider 구현체에서 Account 정보를 조회하기 위해 사용함)
     * @exception InvalidAuthorityException 유효하지 않으면
     */
    private fun tokenToClaims(token: String): Claims {
        return tokenParser.parseClaimsJws(token).body
    }

    private fun expiredAt(issuedAt: LocalDateTime, expSec: Long): LocalDateTime {
        return LocalDateTime
            .from(issuedAt) // copy
            .plusSeconds(expSec)
    }

    private fun toDate(localDateTime: LocalDateTime): Date {
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        return Date.from(instant)
    }

    private fun createNewToken(account: Account, issuedAt: LocalDateTime): String {

        val expiredAt = expiredAt(issuedAt, accessTokenExpTime)
        val claims = Jwts.claims()
            .setIssuer(iss)
            .setIssuedAt(toDate(issuedAt))
            .setExpiration(toDate(expiredAt))
            .setSubject(account.id.toString())

        claims[NAME_KEY] = account.name
        claims[AUTHOR_ID_KEY] = account.authorId
        claims[AUTHORITIES_KEY] = account.authorities.joinToString(",") { a -> a.authority }

        return Jwts.builder()
            .setClaims(claims)
            .signWith(secretKey)
            .compact()
    }
}