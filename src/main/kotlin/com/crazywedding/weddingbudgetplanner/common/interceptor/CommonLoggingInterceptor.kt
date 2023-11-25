package com.crazywedding.weddingbudgetplanner.common.interceptor

import com.crazywedding.weddingbudgetplanner.security.authentication.AuthenticationFacade
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Component
class CommonLoggingInterceptor(
    private val authenticationFacade: AuthenticationFacade
) : HandlerInterceptor {

    private val logger = KotlinLogging.logger {}

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.debug("RequestID: {}", UUID.randomUUID().toString())
        logger.debug("[START] {} {}", request.method, request.requestURI)
        logger.debug("QueryParams: {}", request.queryString)
        logger.info(request.toString())
        val principal = authenticationFacade.getPrincipal()
        logger.debug(
            "Principal: authorities={}, id={}, authorId={}, email={}",
            principal.authorities,
            principal.id,
            principal.authorId,
            principal.name
        )
        logger.debug("Authorization: {}", request.getHeader("Authorization"))

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        logger.debug("[END] {}", request.requestURI)
    }
}