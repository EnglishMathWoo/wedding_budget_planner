package com.crazywedding.weddingbudgetplanner.common.validator

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [PhoneFormatValidator::class])
annotation class PhoneFormat(
    val nullable: Boolean = true,
    val message: String = "전화번호 포맷이 올바르지 않습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)