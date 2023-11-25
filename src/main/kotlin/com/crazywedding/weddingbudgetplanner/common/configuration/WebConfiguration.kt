package com.crazywedding.weddingbudgetplanner.common.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(
    private val commonLoggingInterceptor: HandlerInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(commonLoggingInterceptor)
            .addPathPatterns("/user-api/**")
            .addPathPatterns("/admin-api/**")
            .excludePathPatterns("/user-api/auth/**")
            .excludePathPatterns("/admin-api/auth/**")
    }
}