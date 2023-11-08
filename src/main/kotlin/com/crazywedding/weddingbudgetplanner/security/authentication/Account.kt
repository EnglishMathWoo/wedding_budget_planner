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
    // Security Principal 에서는 getter override 가 강제화 되어있음
    @JvmField val name: String,
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
        // for controller
        fun of(user: UserDto): Account {
            return Account(
                id = user.id,
                name = user.email,
                authorId = user.authorId,
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Authority.ROLE_USER)
            )
        }
        fun of(admin: AdminDto): Account {
            return Account(
                id = admin.id,
                name = admin.email!!,
                authorId = admin.authorId,
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Authority.ROLE_ADMIN)
            )
        }

        // for service
        fun of(user: User): Account {
            return Account(
                id = user.id!!,
                name = user.email,
                authorId = user.author?.id!!,
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Authority.ROLE_USER)
            )
        }
        fun of(admin: Admin): Account {
            return Account(
                id = admin.id!!,
                name = admin.email!!,
                authorId = admin.author?.id!!,
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Authority.ROLE_ADMIN)
            )
        }
    }
}