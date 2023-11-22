package com.crazywedding.weddingbudgetplanner.authentication.service

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserChangePasswordDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserSignInDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserSignUpDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.UserDto

interface UserAccountService {
    fun signUp(dto: UserSignUpDto): UserDto
    fun signIn(dto: UserSignInDto): UserDto
}