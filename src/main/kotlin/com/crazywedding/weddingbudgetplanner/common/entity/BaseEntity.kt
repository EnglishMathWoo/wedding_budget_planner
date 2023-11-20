package com.crazywedding.weddingbudgetplanner.common.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @CreatedBy
    val createdBy: Long? = null,

    @Column
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.MIN,

    @Column
    @LastModifiedBy
    var updatedBy: Long? = null,

    @Column
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.MIN,
)