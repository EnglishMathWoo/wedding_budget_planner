package com.crazywedding.weddingbudgetplanner.authentication.controller.impl

import com.crazywedding.weddingbudgetplanner.authentication.controller.AuthenticationAdminController
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.AdminSignInDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.AdminSignUpDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.TokenAuthorizeDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.AdminWrapperDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.TokenWrapperDto
import com.crazywedding.weddingbudgetplanner.authentication.service.AdminAccountService
import com.crazywedding.weddingbudgetplanner.authentication.service.TokenService
import com.crazywedding.weddingbudgetplanner.security.authentication.Account
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin-api")
class AuthenticationAdminControllerImpl(
    private val adminAccountService: AdminAccountService,
    private val adminTokenService: TokenService
) : AuthenticationAdminController {
    @PostMapping("/auth/sign-in")
    override fun signIn(
        @RequestBody
        dto: AdminSignInDto
    ): TokenWrapperDto {
        val admin = adminAccountService.signIn(dto)
        val token = adminTokenService.create(Account.of(admin))

        return TokenWrapperDto.from(token)
    }

    @PostMapping("/auth/sign-up")
    override fun signUp(
        @Valid @RequestBody
        dto: AdminSignUpDto
    ): AdminWrapperDto {
        val admin = adminAccountService.signUp(dto)

        return AdminWrapperDto.from(admin)
    }

    @PostMapping("/auth/token/refresh")
    override fun refreshToken(
        @Valid @RequestBody
        tokenAuthorizeDto: TokenAuthorizeDto
    ): TokenWrapperDto {
        return TokenWrapperDto.from(adminTokenService.refresh(tokenAuthorizeDto))
    }

    @PostMapping("/auth/sign-out")
    override fun signOut(
        @Valid @RequestBody
        tokenAuthorizeDto: TokenAuthorizeDto
    ) {
        adminTokenService.releaseToken(tokenAuthorizeDto)
    }
}