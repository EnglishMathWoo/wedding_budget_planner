package com.crazywedding.weddingbudgetplanner.authentication.service

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserChangePasswordDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserModifyDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.UserDto

interface UserService {
    fun getMe(id: Long): UserDto
    fun modify(id: Long, dto: UserModifyDto): UserDto
    fun changePassword(id: Long, dto: UserChangePasswordDto): UserDto
}