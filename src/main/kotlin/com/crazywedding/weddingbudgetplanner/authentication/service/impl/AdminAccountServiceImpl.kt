package com.crazywedding.weddingbudgetplanner.authentication.service.impl

import com.crazywedding.weddingbudgetplanner.authentication.dto.request.AdminChangePasswordDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.AdminSignInDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.request.AdminSignUpDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.AdminDto
import com.crazywedding.weddingbudgetplanner.authentication.entity.Admin
import com.crazywedding.weddingbudgetplanner.authentication.entity.Author
import com.crazywedding.weddingbudgetplanner.authentication.repository.AdminRepository
import com.crazywedding.weddingbudgetplanner.authentication.repository.AuthorRepository
import com.crazywedding.weddingbudgetplanner.authentication.service.AdminAccountService
import com.crazywedding.weddingbudgetplanner.common.error.exception.DuplicatedResourceException
import com.crazywedding.weddingbudgetplanner.common.error.exception.IncorrectPasswordException
import com.crazywedding.weddingbudgetplanner.common.error.exception.NotFoundResourceException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminAccountServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val adminRepository: AdminRepository,
    private val authorRepository: AuthorRepository,
) : AdminAccountService {

    @Transactional
    override fun signIn(dto: AdminSignInDto): AdminDto {
        val admin = adminRepository.findAdminByUsername(dto.username)
            ?: throw NotFoundResourceException("username이 일치하는 어드민을 찾지 못했습니다.")

        if (!passwordEncoder.matches(dto.password, admin.encryptedPassword)) {
            throw IncorrectPasswordException("비밀번호가 일치하지 않습니다.")
        }

        return AdminDto.from(admin)!!
    }

    @Transactional
    override fun signUp(dto: AdminSignUpDto): AdminDto {
        if (adminRepository.existsByUsername(dto.username)) {
            throw DuplicatedResourceException("중복된 유저가 존재합니다.")
        }
        val encPassword = encryptPassword(dto.password)
        val newAdmin = adminRepository.save(dto.toEntity(encPassword))
        val newAuthor = authorRepository.save(Author.ofAdmin(newAdmin))
        newAdmin.author = newAuthor

        return AdminDto.from(newAdmin)!!
    }

    @Transactional
    override fun changePassword(id: Long, dto: AdminChangePasswordDto): AdminDto {
        val admin = getAdminAndThrowExIfNotExisted(id)
        if (!matchPassword(dto.currentPassword, admin.encryptedPassword)) {
            throw IncorrectPasswordException("비밀번호가 일치하지 않습니다.")
        }
        admin.encryptedPassword = encryptPassword(dto.newPassword)

        return AdminDto.from(admin)!!
    }

    private fun matchPassword(rawPassword: String, encPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encPassword)
    }

    private fun encryptPassword(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }

    private fun getAdminAndThrowExIfNotExisted(id: Long): Admin {
        return adminRepository.findById(id)
            .orElseThrow { NotFoundResourceException("일치하는 어드민을 찾을 수 없습니다.") }
    }
}