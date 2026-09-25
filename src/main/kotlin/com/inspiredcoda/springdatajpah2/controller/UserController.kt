package com.inspiredcoda.springdatajpah2.controller

import com.inspiredcoda.springdatajpah2.domain.model.UserDto
import com.inspiredcoda.springdatajpah2.domain.service.AuthenticationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class UserController(
    private val authenticationService: AuthenticationService
) {

    @GetMapping("/users")
    fun getUsers(): List<UserDto> {
        return authenticationService.getAllUsers()
    }


}