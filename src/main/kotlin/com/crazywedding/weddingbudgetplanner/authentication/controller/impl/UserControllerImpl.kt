package com.crazywedding.weddingbudgetplanner.authentication.controller.impl

import com.crazywedding.weddingbudgetplanner.authentication.controller.UserController
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserChangePasswordDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.wrapper.UserWrapperDto
import com.crazywedding.weddingbudgetplanner.authentication.service.UserAccountService
import com.crazywedding.weddingbudgetplanner.security.authentication.AuthenticationFacade
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RequestMapping("/user-api/users")
@RestController
class UserControllerImpl(
    private val userAccountService: UserAccountService,
    private val authenticationFacade: AuthenticationFacade
) : UserController {

    @PutMapping("/passwords")
    override fun changeAdminPassword(
        @Valid @RequestBody
        dto: UserChangePasswordDto
    ): UserWrapperDto {
        val userId = authenticationFacade.getPrincipal().id
        val user = userAccountService.changePassword(userId, dto)
        return UserWrapperDto.from(user)
    }
}