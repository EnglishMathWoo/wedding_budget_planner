package com.crazywedding.weddingbudgetplanner.authentication.service

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.TokenAuthorizeDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.TokenDto
import com.crazywedding.weddingbudgetplanner.security.authentication.Account

interface TokenService {

    fun create(account: Account): TokenDto
    fun refreshToken(authorizeDto: TokenAuthorizeDto): TokenDto
    fun releaseToken(authorizeDto: TokenAuthorizeDto)
}