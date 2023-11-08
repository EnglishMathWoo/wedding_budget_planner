package com.crazywedding.weddingbudgetplanner.authentication.entity

import com.crazywedding.weddingbudgetplanner.authentication.entity.enum.AuthorTypeEnum
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
@Table(name = "AUTHOR")
class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @Comment("author type")
    @Enumerated(EnumType.STRING)
    val authorType: AuthorTypeEnum,

    // FIXME: admin, user 필드만 지정하고 타입을 지정하지 않아도 될 것 같으나 추후에 논의하면 좋을듯.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @Comment("어드민 정보")
    val admin: Admin? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @Comment("사용자 정보")
    val user: User? = null,

) {

    companion object {
        fun ofAdmin(admin: Admin) = Author(
            authorType = AuthorTypeEnum.ADMIN,
            admin = admin,
        )
        fun ofUser(user: User) = Author(
            authorType = AuthorTypeEnum.USER,
            user = user,
        )
    }
}