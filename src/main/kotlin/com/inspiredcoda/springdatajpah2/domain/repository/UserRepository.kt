package com.inspiredcoda.springdatajpah2.domain.repository

import com.inspiredcoda.springdatajpah2.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<User, UUID> {

    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?

}