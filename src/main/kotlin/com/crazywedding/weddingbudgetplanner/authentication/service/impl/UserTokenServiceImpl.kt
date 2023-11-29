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
        val jwt = userJwtProvider.create(account, LocalDateTime.now())

        userTokenRepository.save(
            UserToken(
                accessToken = jwt.accessToken,
                refreshToken = jwt.refreshToken,
                userId = account.id
            )
        )
        return TokenDto.from(jwt)
    }

    @Transactional
    override fun refreshToken(authorizeDto: TokenAuthorizeDto): TokenDto {
        val userToken = userTokenRepository.findByRefreshToken(authorizeDto.refreshToken)
            ?: throw InvalidAuthorityException("유효하지 않은 token 입니다.")
        val user = userRepository.findById(userToken.userId)
            .orElseThrow { InvalidAuthorityException("이용이 불가한 유저 계정입니다.") }
        val jwt = userJwtProvider.create(Account.of(user), LocalDateTime.now())

        userTokenRepository.save(
            UserToken(
                accessToken = jwt.accessToken,
                refreshToken = userToken.refreshToken,
                userId = userToken.userId
            )
        )
        return TokenDto.from(jwt)
    }

    @Transactional
    override fun releaseToken(authorizeDto: TokenAuthorizeDto) {
        val userToken = userTokenRepository.findByRefreshToken(authorizeDto.refreshToken)
            ?: throw InvalidAuthorityException("유효하지 않은 token 입니다.")
        userTokenRepository.delete(userToken)
    }
}