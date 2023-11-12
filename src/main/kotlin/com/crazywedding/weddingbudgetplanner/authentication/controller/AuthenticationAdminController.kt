package com.crazywedding.weddingbudgetplanner.authentication.controller

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.*
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.AdminWrapperDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.TokenWrapperDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "어드민 인증", description = "어드민 인증 관련 컨트롤러")
interface AuthenticationAdminController {
    @Operation(summary = "어드민 로그인", description = "로그인 후 token을 발급 받는다.")
    fun signIn(
        dto: AdminSignInDto
    ): TokenWrapperDto

    @Operation(summary = "어드민 계정 생성", description = "어드민 계정을 생성한다.")
    fun signUp(
        @Valid @RequestBody
        dto: AdminSignUpDto
    ): AdminWrapperDto

    @Operation(summary = "accessToken 갱신", description = "access token을 갱신한다.")
    fun refreshToken(
        @RequestBody @Valid tokenAuthorizeDto: TokenAuthorizeDto
    ): TokenWrapperDto

    @Operation(summary = "로그아웃", description = "로그아웃 후 refresh 토큰을 삭제한다.")
    fun signOut(
        @RequestBody @Valid tokenAuthorizeDto: TokenAuthorizeDto
    )
}