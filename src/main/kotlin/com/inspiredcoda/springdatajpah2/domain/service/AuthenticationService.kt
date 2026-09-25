package com.inspiredcoda.springdatajpah2.domain.service

import com.inspiredcoda.springdatajpah2.data.entity.RefreshToken
import com.inspiredcoda.springdatajpah2.domain.model.TokenPair
import com.inspiredcoda.springdatajpah2.data.entity.User
import com.inspiredcoda.springdatajpah2.domain.model.UserDto
import com.inspiredcoda.springdatajpah2.domain.repository.TokenRepository
import com.inspiredcoda.springdatajpah2.domain.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.time.Instant
import java.util.*

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val jwtService: JwtService,
    private val hashEncoder: HashEncoder
): UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails {
        //The Spring framework is expecting us to use the username of our user but in our case, the id is what we use to uniquely identify our user
        val user = userRepository.findById(UUID.fromString(username)).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        return user
    }

    fun registerUser(
        username: String,
        email: String,
        password: String,
        role: User.UserRole
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

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email) ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid credentials"
        )
        val isValid = BCrypt.checkpw(password, user.hashedPassword)

        if (!isValid) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        val accessToken = jwtService.generateAccessToken(user.id, user.role.name)
        val refreshToken = jwtService.generateRefreshToken(user.id, user.role.name)

        tokenRepository.save(
            RefreshToken(
                userId = user.id,
                hashedToken = hashToken(refreshToken),
                expiresIn = Instant.now().plusSeconds(JwtService.REFRESH_TOKEN_VALIDITY_MILLIS),
                createdAt = Instant.now()
            )
        )
        
        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun refresh(refreshToken: String): TokenPair {
        val isValid = jwtService.validateRefreshToken(refreshToken)
        if (!isValid) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }

        val userIdFromToken = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(userIdFromToken).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }

        val refreshToken = tokenRepository.findByUserIdAndHashedToken(userIdFromToken, hashToken(refreshToken))
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")

        val isTokenExpired = refreshToken.expiresIn < Instant.now()
        if (isTokenExpired) {
            tokenRepository.delete(refreshToken)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh token expired")
        }

        if (user.id != userIdFromToken) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id, user.role.name)
        val newRefreshToken = jwtService.generateRefreshToken(user.id, user.role.name)

        tokenRepository.save(
            RefreshToken(
                user.id,
                hashToken(newRefreshToken),
                expiresIn = Instant.now().plusSeconds(JwtService.REFRESH_TOKEN_VALIDITY_MILLIS),
                createdAt = Instant.now()
            )
        )

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun hashToken(value: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = messageDigest.digest(value.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashedBytes)
    }

}