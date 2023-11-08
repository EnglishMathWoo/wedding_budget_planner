package com.crazywedding.weddingbudgetplanner.security.token

import org.springframework.security.core.Authentication
import java.time.LocalDateTime

interface AuthTokenProvider<T, P> {

    /**
     * 계정 정보를 통한 토큰 발급
     */
    fun create(principal: P, issuedAt: LocalDateTime): T
    /**
     * 토큰을 이용하여 인증객체 생성
     * @exception InvalidAuthorityException 유효하지 않으면
     */
    fun authenticate(token: String): Authentication
    /**
     * 토큰 유효성 검증
     * @exception InvalidAuthorityException 유효하지 않으면
     */
    fun validate(token: String)
}