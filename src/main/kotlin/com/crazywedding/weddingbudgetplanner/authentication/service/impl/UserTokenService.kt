package com.crazywedding.weddingbudgetplanner.authentication.service.impl

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.TokenAuthorizeDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.TokenDto
import com.crazywedding.weddingbudgetplanner.authentication.entity.AuthorizedJwt
import com.crazywedding.weddingbudgetplanner.authentication.entity.enum.AccountTypeEnum
import com.crazywedding.weddingbudgetplanner.authentication.repository.AuthorizedJwtRepository
import com.crazywedding.weddingbudgetplanner.authentication.repository.UserRepository
import com.crazywedding.weddingbudgetplanner.authentication.service.TokenService
import com.crazywedding.weddingbudgetplanner.common.error.exception.InvalidAuthorityException
import com.crazywedding.weddingbudgetplanner.common.error.exception.InvalidTokenException
import com.crazywedding.weddingbudgetplanner.security.authentication.Account
import com.crazywedding.weddingbudgetplanner.security.crypto.SymmetricCrypto
import com.crazywedding.weddingbudgetplanner.security.token.JwtProvider
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service("userTokenService")
class UserTokenService(
    val userJwtProvider: JwtProvider,
    val symmetricCrypto: SymmetricCrypto,
    val authorizedJwtRepository: AuthorizedJwtRepository,
    val userRepository: UserRepository,
    @Value("\${authentication.user.refresh-token.expired-seconds}")
    val refreshTokenExpSec: Long,
    @Value("\${authentication.user.access-token.expired-seconds}")
    val accessTokenExpSec: Long
) : TokenService {
    @Transactional
    override fun create(account: Account): TokenDto {
        val issuedAt = LocalDateTime.now()
        val jwt = userJwtProvider.create(account, issuedAt)

        authorizedJwtRepository.save(
            AuthorizedJwt(
                accessToken = jwt.accessToken,
                encRefreshToken = symmetricCrypto.encrypt(jwt.refreshToken),
                expiredAt = LocalDateTime.from(issuedAt).plusSeconds(refreshTokenExpSec),
                issuedAt = issuedAt,
                accountId = account.id,
                accountType = AccountTypeEnum.USER
            )
        )
        return TokenDto.from(jwt)
    }

    @Transactional
    override fun create(account: Account, expSec: Long): TokenDto {
        val issuedAt = LocalDateTime.now()
            .minusSeconds(accessTokenExpSec)
            .plusSeconds(expSec)
        val jwt = userJwtProvider.create(account, issuedAt)
        authorizedJwtRepository.save(
            AuthorizedJwt(
                accessToken = jwt.accessToken,
                encRefreshToken = symmetricCrypto.encrypt(jwt.refreshToken),
                expiredAt = LocalDateTime.from(issuedAt).plusSeconds(refreshTokenExpSec),
                issuedAt = issuedAt,
                accountId = account.id,
                accountType = AccountTypeEnum.USER
            )
        )
        return TokenDto.from(jwt)
    }

    @Transactional(noRollbackFor = [InvalidTokenException::class] /*만료된 refresh 토큰의 경우에는 토큰을 삭제하기 위함*/)
    override fun refresh(authorizeDto: TokenAuthorizeDto): TokenDto {
        val accountId = this.releaseToken(authorizeDto)

        val user = userRepository.findById(accountId).orElseThrow {
            // 인증(토큰 생성) 시점에는 유저가 존재하였으나 리프레시 시점에는 유저가 존재하지 않는 경우
            throw InvalidAuthorityException("이용이 불가한 유저 계정입니다.")
        }
        return this.create(Account.of(user))
    }

    @Transactional
    override fun releaseToken(authorizeDto: TokenAuthorizeDto): Long {
        val authorizedJwt = authorizedJwtRepository.findById(authorizeDto.accessToken).orElseThrow {
            throw InvalidAuthorityException("Invalid Token")
        }

        this.validateRefreshToken(authorizeDto.refreshToken, authorizedJwt)
        this.validateAccessToken(authorizeDto.accessToken)
        authorizedJwtRepository.delete(authorizedJwt)
        return authorizedJwt.accountId
    }

    private fun validateRefreshToken(rawToken: String, authorizedJwt: AuthorizedJwt) {
        if (symmetricCrypto.decrypt(authorizedJwt.encRefreshToken) != rawToken) {
            throw InvalidAuthorityException("Invalid Refresh Token")
        }
        if (authorizedJwt.expiredAt.isBefore(LocalDateTime.now())) {
            throw InvalidTokenException("Already expired refresh token")
        }
    }

    private fun validateAccessToken(accessToken: String) {
        try {
            userJwtProvider.validate(accessToken)
        } catch (e: ExpiredJwtException) {
            // do nothing (ignore ExpiredJwtException)
        }
    }
}