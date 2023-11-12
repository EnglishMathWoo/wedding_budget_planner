package com.crazywedding.weddingbudgetplanner.authentication.service

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.AdminChangePasswordDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.AdminSignInDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.AdminSignUpDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.AdminDto

interface AdminAccountService {
    fun signIn(dto: AdminSignInDto): AdminDto
    fun signUp(dto: AdminSignUpDto): AdminDto
    fun changePassword(id: Long, dto: AdminChangePasswordDto): AdminDto
}