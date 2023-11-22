package com.crazywedding.weddingbudgetplanner.authentication.controller.impl

import com.crazywedding.weddingbudgetplanner.authentication.controller.UserController
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserChangePasswordDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserModifyDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.UserWrapperDto
import com.crazywedding.weddingbudgetplanner.authentication.service.UserAccountService
import com.crazywedding.weddingbudgetplanner.authentication.service.UserService
import com.crazywedding.weddingbudgetplanner.security.authentication.AuthenticationFacade
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RequestMapping("/user-api/users")
@RestController
class UserControllerImpl(
    private val userService: UserService,
    private val authenticationFacade: AuthenticationFacade
) : UserController {

    @GetMapping("/me")
    override fun getMe(): UserWrapperDto {
        val userId = authenticationFacade.getPrincipal().id
        val user = userService.getMe(userId)
        return UserWrapperDto.from(user)
    }

    @PatchMapping("/me")
    override fun modifyUser(
        @Valid @RequestBody
        dto: UserModifyDto
    ): UserWrapperDto {
        val userId = authenticationFacade.getPrincipal().id
        val user = userService.modify(userId, dto)
        return UserWrapperDto.from(user)
    }

    @PutMapping("/passwords")
    override fun changeUserPassword(
        @Valid @RequestBody
        dto: UserChangePasswordDto
    ): UserWrapperDto {
        val userId = authenticationFacade.getPrincipal().id
        val user = userService.changePassword(userId, dto)
        return UserWrapperDto.from(user)
    }
}