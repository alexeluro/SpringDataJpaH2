package com.inspiredcoda.springdatajpah2.controller.model

import com.inspiredcoda.springdatajpah2.data.entity.User.UserRole
import java.util.UUID

data class RegisterUserRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)
