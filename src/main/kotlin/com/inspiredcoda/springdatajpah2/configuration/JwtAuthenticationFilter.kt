package com.inspiredcoda.springdatajpah2.configuration

import com.inspiredcoda.springdatajpah2.domain.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


/**
 *
 * */

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
        token?.let { bearerToken ->
            if (bearerToken.startsWith("Bearer")) {
                val userId = jwtService.getUserIdFromToken(token = bearerToken)

                // THIS IS ONLY SO WE CAN ACCESS THE AUTHENTICATED USER's ID ACROSS THE APP
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(userId, null, emptyList())
            }
        }

        filterChain.doFilter(request, response)
    }

}