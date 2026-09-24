package com.inspiredcoda.springdatajpah2.controller

import com.inspiredcoda.springdatajpah2.controller.model.LoginRequest
import com.inspiredcoda.springdatajpah2.controller.model.RegisterUserRequest
import com.inspiredcoda.springdatajpah2.domain.model.UserDto
import com.inspiredcoda.springdatajpah2.domain.service.AuthenticationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class AuthController(
    val authenticationService: AuthenticationService
) {

    @GetMapping("")
    fun helloWorld(): String {
        return "Hello World"
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody userRequest: RegisterUserRequest): UserDto {
        val newUser = authenticationService.registerUser(
            userRequest.username,
            userRequest.email,
            userRequest.password
        )

        return newUser
    }

    @GetMapping("/auth/users")
    fun getUsers(): List<UserDto> {
        return authenticationService.getAllUsers()
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): UserDto {
        val user = authenticationService.login(
            loginRequest.email,
            loginRequest.password
        )

        return user
    }

}