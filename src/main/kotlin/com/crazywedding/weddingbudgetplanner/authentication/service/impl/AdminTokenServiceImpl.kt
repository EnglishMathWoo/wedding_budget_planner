package com.crazywedding.weddingbudgetplanner.authentication.service.impl

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.TokenAuthorizeDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.TokenDto
import com.crazywedding.weddingbudgetplanner.authentication.entity.AuthorizedJwt
import com.crazywedding.weddingbudgetplanner.authentication.entity.enum.AccountTypeEnum
import com.crazywedding.weddingbudgetplanner.authentication.repository.AdminRepository
import com.crazywedding.weddingbudgetplanner.authentication.repository.AuthorizedJwtRepository
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

@Service("adminTokenService")
class AdminTokenServiceImpl(
    val adminJwtProvider: JwtProvider,
    val symmetricCrypto: SymmetricCrypto,
    val authorizedJwtRepository: AuthorizedJwtRepository,
    val adminRepository: AdminRepository,
    @Value("\${authentication.admin.refresh-token.expired-seconds}")
    val refreshTokenExpSec: Long
) : TokenService {

    @Transactional
    override fun create(account: Account): TokenDto {
        val issuedAt = LocalDateTime.now()
        val jwt = adminJwtProvider.create(account, issuedAt)

        authorizedJwtRepository.save(
            AuthorizedJwt(
                accessToken = jwt.accessToken,
                encRefreshToken = symmetricCrypto.encrypt(jwt.refreshToken),
                expiredAt = LocalDateTime.from(issuedAt).plusSeconds(refreshTokenExpSec),
                issuedAt = issuedAt,
                accountId = account.id,
                accountType = AccountTypeEnum.ADMIN
            )
        )

        return TokenDto.from(jwt)
    }

    @Transactional(noRollbackFor = [InvalidTokenException::class])
    override fun refresh(authorizeDto: TokenAuthorizeDto): TokenDto {
        val accountId = releaseToken(authorizeDto)
        val admin = adminRepository.findById(accountId)
            .orElseThrow { InvalidAuthorityException("이용이 불가한 어드민 계정입니다.") }

        return create(Account.of(admin))
    }

    @Transactional
    override fun releaseToken(authorizeDto: TokenAuthorizeDto): Long {
        val authorizedJwt = authorizedJwtRepository.findById(authorizeDto.accessToken)
            .orElseThrow { InvalidAuthorityException("유효하지 않은 token 입니다.") }
        validateRefreshToken(authorizeDto.refreshToken, authorizedJwt)
        validateAccessToken(authorizeDto.accessToken)
        authorizedJwtRepository.delete(authorizedJwt)

        return authorizedJwt.accountId
    }

    private fun validateRefreshToken(rawToken: String, authorizedJwt: AuthorizedJwt) {
        if (symmetricCrypto.decrypt(authorizedJwt.encRefreshToken) != rawToken) {
            throw InvalidAuthorityException("유효하지 않은 refresh token 입니다.")
        }
        if (authorizedJwt.expiredAt.isBefore(LocalDateTime.now())) {
            throw InvalidTokenException("이미 만료된 refresh token 입니다.")
        }
    }

    private fun validateAccessToken(accessToken: String) {
        try {
            adminJwtProvider.validate(accessToken)
        } catch (e: ExpiredJwtException) {
            // do nothing (ignore ExpiredJwtException)
        }
    }
}