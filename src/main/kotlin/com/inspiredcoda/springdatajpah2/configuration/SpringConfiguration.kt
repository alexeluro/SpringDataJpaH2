package com.inspiredcoda.springdatajpah2.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SpringConfiguration(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            // Never disable csrf without SessionCreationPolicy as STATELESS to avoid a csrf attack
            .csrf { it.disable() }
            .cors { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/v1/auth/**", "/h2-console/**").permitAll()
                    .dispatcherTypeMatchers(
                        DispatcherType.ERROR,
                        DispatcherType.FORWARD
                    ).permitAll()
//                    .requestMatchers("/api/v1/users/").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
            }
            .sessionManagement {  session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .httpBasic(Customizer.withDefaults())
            .build()
    }

}