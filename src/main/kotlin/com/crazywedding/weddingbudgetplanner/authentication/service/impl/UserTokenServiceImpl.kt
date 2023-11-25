package com.crazywedding.weddingbudgetplanner.authentication.service.impl

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.TokenAuthorizeDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.TokenDto
import com.crazywedding.weddingbudgetplanner.authentication.entity.UserToken
import com.crazywedding.weddingbudgetplanner.authentication.repository.UserRepository
import com.crazywedding.weddingbudgetplanner.authentication.repository.UserTokenRepository
import com.crazywedding.weddingbudgetplanner.authentication.service.TokenService
import com.crazywedding.weddingbudgetplanner.common.error.exception.InvalidAuthorityException
import com.crazywedding.weddingbudgetplanner.common.error.exception.InvalidTokenException
import com.crazywedding.weddingbudgetplanner.security.authentication.Account
import com.crazywedding.weddingbudgetplanner.security.token.JwtProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service("userTokenService")
class UserTokenServiceImpl(
    val userJwtProvider: JwtProvider,
    val userTokenRepository: UserTokenRepository,
    val userRepository: UserRepository,
) : TokenService {

    @Transactional
    override fun create(account: Account): TokenDto {
        val issuedAt = LocalDateTime.now()
        val jwt = userJwtProvider.create(account, issuedAt)

        userTokenRepository.save(
            UserToken(
                accessToken = jwt.accessToken,
                refreshToken = jwt.refreshToken,
                userId = account.id
            )
        )
        return TokenDto.from(jwt)
    }

    @Transactional(noRollbackFor = [InvalidTokenException::class])
    override fun refreshToken(authorizeDto: TokenAuthorizeDto): TokenDto {
        val accountId = releaseToken(authorizeDto)
        val user = userRepository.findById(accountId)
            .orElseThrow { InvalidAuthorityException("이용이 불가한 유저 계정입니다.") }
        return create(Account.of(user))
    }

    @Transactional
    override fun releaseToken(authorizeDto: TokenAuthorizeDto): Long {
        val userToken = userTokenRepository.findByRefreshToken(authorizeDto.refreshToken)
            ?: throw InvalidAuthorityException("유효하지 않은 token 입니다.")
        validateToken(authorizeDto.refreshToken, userToken)
        userTokenRepository.delete(userToken)
        return userToken.userId
    }

    private fun validateToken(rawToken: String, authorizedJwt: UserToken) {
        if (authorizedJwt.refreshToken != rawToken) {
            throw InvalidAuthorityException("유효하지 않은 refresh token 입니다.")
        }
        userJwtProvider.validate(authorizedJwt.accessToken)
    }
}