package com.inspiredcoda.springdatajpah2.domain.model

import com.inspiredcoda.springdatajpah2.data.entity.User.UserRole
import java.util.UUID

data class UserDto(
    val id: UUID,
    val username: String,
    val email: String,
    val role: UserRole
)
