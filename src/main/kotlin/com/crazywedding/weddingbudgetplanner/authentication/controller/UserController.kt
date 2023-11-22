package com.crazywedding.weddingbudgetplanner.authentication.controller

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserChangePasswordDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserModifyDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.UserWrapperDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "유저", description = "유저 CRUD")
interface UserController {

    @Operation(summary = "내 정보 조회", description = "내 정보를 조회한다.")
    fun getMe(): UserWrapperDto

    @Operation(summary = "내 정보 수정", description = "내 정보를 수정한다.")
    fun modifyUser(
        dto: UserModifyDto
    ): UserWrapperDto

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경한다.")
    fun changeUserPassword(
        dto: UserChangePasswordDto
    ): UserWrapperDto
}