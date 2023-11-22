package com.crazywedding.weddingbudgetplanner.authentication.service.impl

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserChangePasswordDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.UserModifyDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.UserDto
import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import com.crazywedding.weddingbudgetplanner.authentication.repository.UserRepository
import com.crazywedding.weddingbudgetplanner.authentication.service.UserService
import com.crazywedding.weddingbudgetplanner.common.error.exception.IncorrectPasswordException
import com.crazywedding.weddingbudgetplanner.common.error.exception.NotFoundResourceException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserService {

    @Transactional
    override fun getMe(id: Long): UserDto {
        val user = getUserAndThrowExIfNotExisted(id)
        return UserDto.from(user)
    }

    override fun modify(id: Long, dto: UserModifyDto): UserDto {
        val user = getUserAndThrowExIfNotExisted(id)
        dto.modifyEntity(user)
        return UserDto.from(user)
    }

    @Transactional
    override fun changePassword(id: Long, dto: UserChangePasswordDto): UserDto {
        val user = getUserAndThrowExIfNotExisted(id)
        if (!passwordEncoder.matches(dto.currentPassword, user.encryptedPassword)) {
            throw IncorrectPasswordException("비밀번호가 일치하지 않습니다.")
        }
        user.encryptedPassword = passwordEncoder.encode(dto.newPassword)
        return UserDto.from(user)
    }

    private fun getUserAndThrowExIfNotExisted(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { NotFoundResourceException("일치하는 유저를 찾을 수 없습니다.") }
    }
}