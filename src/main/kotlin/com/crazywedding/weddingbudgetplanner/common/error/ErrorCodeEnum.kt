package com.crazywedding.weddingbudgetplanner.common.error

enum class ErrorCodeEnum(
    val errorCode: String,
    val description: String
) {
    DUPLICATED_RESOURCE("40900", "이미 생성된 자원을 생성하려고 했을때 발생하는 예외 처리"),
    EXPIRED_TOKEN("40100", "토큰의 유효기간이 만료되었을때 발생하는 예외 처리"),
    INVALID_AUTHORITY("40101", "권한이 없을 경우 예외 처리"),
    ACCESS_DENIED_RESOURCE("40301", "허가되지 않은 자원 접근시 예외 처리"),
    EXTERNAL_SERVER("50000", "외부 서비스 에러시 발생하는 예외 처리"),
    INCORRECT_PASSWORD("40101", "비밀번호가 미일치 발생하는 예외 처리"),
    INTERNAL_SERVER("50001", "내부 서비스 에러시 발생하는 예외 처리"),
    INVALID_REQUEST_PARAMETER("42200", "요청하는 파라미터의 값이 올바르지 않을때 발생하는 예외 처리"),
    INVALID_TOKEN("40102", "토큰이 형식이 올바르지 않는 경우 발생하는 예외 처리"),
    NOT_FOUND_RESOURCE("40400", "자원을 찾을 수 없을 때 발생하는 예외 처리"),
    NOT_IMPLEMENTATION("50100", "구현되지 않는 api 요청시 발생하는 예외 처리"),
    INVALID_BASE_ENTITY_PROPERTY("50101", "반드시 생성되어야 하는 base entity property가 존재하지 않을때 발생하는 예외처리")
}