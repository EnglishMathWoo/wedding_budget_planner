package com.crazywedding.weddingbudgetplanner.authentication.service.impl

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.*
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.UserDto
import com.crazywedding.weddingbudgetplanner.authentication.entity.Author
import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import com.crazywedding.weddingbudgetplanner.authentication.repository.AuthorRepository
import com.crazywedding.weddingbudgetplanner.authentication.repository.UserRepository
import com.crazywedding.weddingbudgetplanner.authentication.service.UserAccountService
import com.crazywedding.weddingbudgetplanner.common.error.exception.DuplicatedResourceException
import com.crazywedding.weddingbudgetplanner.common.error.exception.IncorrectPasswordException
import com.crazywedding.weddingbudgetplanner.common.error.exception.NotFoundResourceException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAccountServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authorRepository: AuthorRepository,
) : UserAccountService {

    @Transactional
    override fun signIn(dto: UserSignInDto): UserDto {
        val user = userRepository.findByUsername(dto.username)
            ?: throw NotFoundResourceException("username이 일치하는 어드민을 찾지 못했습니다.")

        if (!passwordEncoder.matches(dto.password, user.encryptedPassword)) {
            throw IncorrectPasswordException("비밀번호가 일치하지 않습니다.")
        }
        return UserDto.from(user)
    }

    @Transactional
    override fun signUp(dto: UserSignUpDto): UserDto {
        if (userRepository.existsByUsername(dto.username)) {
            throw DuplicatedResourceException("중복된 유저가 존재합니다.")
        }
        val encPassword = encryptPassword(dto.password)
        val newUser = dto.toEntity(encPassword)

        userRepository.save(newUser)
        val newAuthor = authorRepository.save(Author.ofUser(newUser))
        newUser.author = newAuthor
        return UserDto.from(newUser)
    }

    @Transactional
    override fun changePassword(id: Long, dto: UserChangePasswordDto): UserDto {
        val user = getUserAndThrowExIfNotExisted(id)
        if (!matchPassword(dto.currentPassword, user.encryptedPassword)) {
            throw IncorrectPasswordException("비밀번호가 일치하지 않습니다.")
        }
        user.encryptedPassword = encryptPassword(dto.newPassword)
        return UserDto.from(user)
    }

    private fun matchPassword(rawPassword: String, encPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encPassword)
    }

    private fun encryptPassword(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }

    private fun getUserAndThrowExIfNotExisted(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { NotFoundResourceException("일치하는 유저를 찾을 수 없습니다.") }
    }
}