package com.crazywedding.weddingbudgetplanner.authentication.entity

import com.crazywedding.weddingbudgetplanner.common.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
@Table(name = "USER")
class User(
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
    @Comment(value = "휴대폰번호")
    var phone: String,

    @Column
    @Comment(value = "이메일")
    var email: String?,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    var author: Author? = null,

) : BaseEntity()