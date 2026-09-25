package com.inspiredcoda.springdatajpah2.controller

import com.inspiredcoda.springdatajpah2.controller.model.LoginRequest
import com.inspiredcoda.springdatajpah2.controller.model.RefreshTokenRequest
import com.inspiredcoda.springdatajpah2.controller.model.RegisterUserRequest
import com.inspiredcoda.springdatajpah2.domain.model.TokenPair
import com.inspiredcoda.springdatajpah2.domain.model.UserDto
import com.inspiredcoda.springdatajpah2.domain.service.AuthenticationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth/")
class AuthController(
    val authenticationService: AuthenticationService
) {

    @GetMapping("")
    fun helloWorld(): String {
        return "Hello World"
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody userRequest: RegisterUserRequest): UserDto {

        //TODO: validation is supposed to be run on the email and password

        val newUser = authenticationService.registerUser(
            userRequest.username,
            userRequest.email,
            userRequest.password,
            userRequest.role
        )

        return newUser
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): TokenPair {
        val tokenPair = authenticationService.login(
            loginRequest.email,
            loginRequest.password
        )

        return tokenPair
    }

}