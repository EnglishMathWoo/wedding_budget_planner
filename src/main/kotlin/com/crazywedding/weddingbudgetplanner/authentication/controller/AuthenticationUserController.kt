package com.crazywedding.weddingbudgetplanner.authentication.controller

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.*
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.TokenWrapperDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.UserWrapperDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "유저 인증", description = "유저 인증 관련 컨트롤러")
interface AuthenticationUserController {
    @Operation(summary = "유저 로그인", description = "로그인 후 token을 발급 받는다.")
    fun signIn(
        dto: UserSignInDto
    ): TokenWrapperDto

    @Operation(summary = "유저 계정 생성", description = "유저 계정을 생성한다.")
    fun signUp(
        @Valid @RequestBody
        dto: UserSignUpDto
    ): UserWrapperDto

    @Operation(summary = "accessToken 갱신", description = "access token을 갱신한다.")
    fun refreshToken(
        @RequestBody @Valid
        tokenAuthorizeDto: TokenAuthorizeDto
    ): TokenWrapperDto

    @Operation(summary = "로그아웃", description = "로그아웃 후 refresh 토큰을 삭제한다.")
    fun signOut(
        @RequestBody @Valid
        tokenAuthorizeDto: TokenAuthorizeDto
    )
}