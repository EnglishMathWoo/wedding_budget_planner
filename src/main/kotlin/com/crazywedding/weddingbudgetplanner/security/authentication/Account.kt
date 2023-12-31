package com.crazywedding.weddingbudgetplanner.security.authentication

import com.crazywedding.weddingbudgetplanner.authentication.dto.response.AdminDto
import com.crazywedding.weddingbudgetplanner.authentication.dto.response.UserDto
import com.crazywedding.weddingbudgetplanner.authentication.entity.Admin
import com.crazywedding.weddingbudgetplanner.authentication.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import java.security.Principal

class Account(
    val id: Long,
    @JvmField
    val name: String,
    val authorId: Long,
    val authorities: List<GrantedAuthority>
) : Principal {

    override fun getName(): String {
        return name
    }

    companion object {

        fun anonymous(): Account {
            return Account(
                id = -1,
                name = "anonymous",
                authorId = -1,
                authorities = AuthorityUtils.NO_AUTHORITIES
            )
        }

        fun of(user: UserDto): Account {
            return Account(
                id = user.id,
                name = user.username,
                authorId = user.authorId,
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Authority.ROLE_USER)
            )
        }

        fun of(admin: AdminDto): Account {
            return Account(
                id = admin.id,
                name = admin.username,
                authorId = admin.authorId,
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Authority.ROLE_ADMIN)
            )
        }

        fun of(user: User): Account {
            return Account(
                id = user.id!!,
                name = user.username,
                authorId = user.author?.id!!,
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Authority.ROLE_USER)
            )
        }

        fun of(admin: Admin): Account {
            return Account(
                id = admin.id!!,
                name = admin.username,
                authorId = admin.author?.id!!,
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Authority.ROLE_ADMIN)
            )
        }
    }
}