package com.crazywedding.weddingbudgetplanner.authentication.controller.impl

import com.crazywedding.weddingbudgetplanner.authentication.controller.AuthenticationUserController
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.*
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.TokenWrapperDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.UserWrapperDto
import com.crazywedding.weddingbudgetplanner.authentication.service.TokenService
import com.crazywedding.weddingbudgetplanner.authentication.service.UserAccountService
import com.crazywedding.weddingbudgetplanner.security.authentication.Account
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user-api")
class AuthenticationUserControllerImpl(
    private val userAccountService: UserAccountService,
    private val userTokenService: TokenService,
) : AuthenticationUserController {
    @PostMapping("/auth/sign-in")
    override fun signIn(
        @RequestBody
        dto: UserSignInDto
    ): TokenWrapperDto {
        val user = userAccountService.signIn(dto)
        val token = userTokenService.create(Account.of(user))

        return TokenWrapperDto.from(token)
    }

    @PostMapping("/auth/sign-up")
    override fun signUp(
        @Valid @RequestBody
        dto: UserSignUpDto
    ): UserWrapperDto {
        val user = userAccountService.signUp(dto)

        return UserWrapperDto.from(user)
    }

    @PostMapping("/auth/token/refresh")
    override fun refreshToken(
        @Valid @RequestBody
        tokenAuthorizeDto: TokenAuthorizeDto
    ): TokenWrapperDto {
        return TokenWrapperDto.from(userTokenService.refresh(tokenAuthorizeDto))
    }

    @PostMapping("/auth/sign-out")
    override fun signOut(
        @Valid @RequestBody
        tokenAuthorizeDto: TokenAuthorizeDto
    ) {
        userTokenService.releaseToken(tokenAuthorizeDto)
    }
}