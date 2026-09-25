package com.inspiredcoda.springdatajpah2.domain.service

import com.inspiredcoda.springdatajpah2.data.entity.User
import com.inspiredcoda.springdatajpah2.data.entity.User.UserRole
import com.inspiredcoda.springdatajpah2.domain.model.UserDto
import com.inspiredcoda.springdatajpah2.domain.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class AuthenticationService(
    private val userRepository: UserRepository
) {

    fun registerUser(
        username: String,
        email: String,
        password: String,
    ): UserDto {
        val userWithEmailExists = userRepository.findByEmail(email) != null
        if (userWithEmailExists) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "email is not available")
        }

        val userWithUsernameExists = userRepository.findByUsername(username) != null
        if (userWithUsernameExists) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "username is not available")
        }

        val newUser = User(
            id = UUID.randomUUID(),
            username = username,
            email = email,
            hashedPassword = hashEncoder.hash(password),
            role = role
        )

        val savedUser = userRepository.save(newUser)

        return savedUser.toUserDto()
    }

    private fun passwordEncoder(value: String): String {
        return BCrypt.hashpw(value, BCrypt.gensalt())
    }

    fun User.toUserDto(): UserDto {
        return UserDto(
            id = id,
            username = username,
            email = email,
            role = role
        )
    }

    fun getAllUsers(): List<UserDto> {
        //TODO: Only ADMINs should be allowed to fetch this data
        return userRepository.findAll().map { it.toUserDto() }
    }

    fun login(email: String, password: String): UserDto {
        val user = userRepository.findByEmail(email) ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid credentials"
        )
        val isValid = BCrypt.checkpw(password, user.hashedPassword)

        if (!isValid) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        return user.toUserDto()
    }

}