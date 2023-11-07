package com.crazywedding.weddingbudgetplanner.security.configuration

import com.crazywedding.weddingbudgetplanner.security.filter.GlobalBaseExceptionFilter
import com.crazywedding.weddingbudgetplanner.security.filter.UserAccessTokenFilter
import com.crazywedding.weddingbudgetplanner.security.filter.UserRefreshTokenFilter
import com.crazywedding.weddingbudgetplanner.security.handler.DelegatedAccessDeniedHandler
import com.crazywedding.weddingbudgetplanner.security.handler.DelegatedAuthenticationEntryPoint
import com.crazywedding.weddingbudgetplanner.security.principal.Authority
import com.crazywedding.weddingbudgetplanner.security.token.UserAccessTokenProvider
import com.crazywedding.weddingbudgetplanner.security.token.UserRefreshTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    @Throws(Exception::class)
    fun filterChain(
        http: HttpSecurity,
        userAccessTokenProvider: UserAccessTokenProvider,
        userRefreshTokenProvider: UserRefreshTokenProvider,
        accessDeniedHandler: DelegatedAccessDeniedHandler,
        authenticationEntryPoint: DelegatedAuthenticationEntryPoint,
    ): SecurityFilterChain {
        http.csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .requestMatchers(
                        "/user-api/auth/**",
                        "/user-api/oauth/**",
                        "/admin-api/auth/**",
                        "/admin-api/main/**"
                    ).permitAll()
                    .requestMatchers("/user-api/**").hasAnyRole(Authority.USER)
                    .requestMatchers("/admin-api/**").hasAnyRole(Authority.ADMIN)
                    .requestMatchers("/**").permitAll()
            }.addFilterBefore(
                GlobalBaseExceptionFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterBefore(
                UserAccessTokenFilter(userAccessTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterBefore(
                UserRefreshTokenFilter(userRefreshTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling {
                it
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(authenticationEntryPoint)
            }


        return http.build()
    }

    fun environmentalAllowedOriginPatterns(): List<String>? {
        return listOf(
            "http://localhost:[*]"
        )
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configSource = UrlBasedCorsConfigurationSource()
        val corsConfig = CorsConfiguration()
        corsConfig.allowCredentials = true // js 요청에서 credentials가 include일 때 응답하기 위해 true로 설정
        corsConfig.allowedOriginPatterns = environmentalAllowedOriginPatterns() // 요청을 허용할 ip 설정
        corsConfig.exposedHeaders = listOf() // client가 읽을 수 있는 헤더 값 리스트 설정
        corsConfig.addAllowedHeader("*") // 모든 header 요청 허용
        corsConfig.addAllowedMethod("*") // 모든 post,get,put,delete,patch 요청 허용
        configSource.registerCorsConfiguration("/**", corsConfig) // 모든 요청에 대해 corsConfig 적용
        return configSource
    }
}