package com.crazywedding.weddingbudgetplanner.security.handler

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication

interface AuthenticateHandler {

    fun authenticate(request: HttpServletRequest): Authentication
}