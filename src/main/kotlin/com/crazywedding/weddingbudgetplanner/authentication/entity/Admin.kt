package com.crazywedding.weddingbudgetplanner.authentication.entity

import com.crazywedding.weddingbudgetplanner.common.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
@Table(name = "ADMIN")
class Admin(
    @Column
    @Comment(value = "아이디")
    val username: String,

    @Column
    @Comment(value = "암호화된 비밀번호")
    var encryptedPassword: String,

    @Column
    @Comment(value = "이름")
    var name: String,

    @Column
    @Comment(value = "전화번호")
    var phone: String?,

    @Column
    @Comment(value = "이메일")
    var email: String?,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "admin")
    var author: Author? = null,

) : BaseEntity()