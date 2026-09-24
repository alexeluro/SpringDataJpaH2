package com.inspiredcoda.springdatajpah2.domain.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret.base64}") private val jwtSecretBase64: String,
) {

    private val JWT_SECRET = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretBase64))

    private fun generateToken(
        userId: UUID,
        type: String,
        validityInMs: Long,
    ): String {
        val issueDate = Date()
        val expiryDate = Date(issueDate.time + validityInMs)

        return Jwts.builder()
            .subject(userId.toString())
            .claim("type", type)
            .issuedAt(issueDate)
            .expiration(expiryDate)
            .signWith(JWT_SECRET, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(
        userid: UUID
    ): String {
        return generateToken(userid, "access", ACCESS_TOKEN_VALIDITY_MILLIS)
    }

    fun generateRefreshToken(
        userid: UUID
    ): String {
        return generateToken(userid, "refresh", REFRESH_TOKEN_VALIDITY_MILLIS)
    }

    private fun extractRawToken(token: String): String {
        return if (token.startsWith("Bearer ")) {
            token.removePrefix("Bearer ").trim()
        } else {
            token.trim()
        }
    }

    fun validateAccessToken(token: String): Boolean {
        val claims = parseClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "refresh"
    }

    fun getUserIdFromToken(token: String): UUID {
        val claims = parseClaims(token) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")

        // We are sure that the subject is our UUID because that's what we passed in when we created the token in the generateToken function
        return UUID.fromString(claims.subject)
    }

    private fun parseClaims(token: String): Claims? {
        return try {
            val rawToken = extractRawToken(token)
            Jwts.parser()
                .verifyWith(JWT_SECRET)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (ex: Exception) {
            null
        }
    }

    companion object{
        private val ACCESS_TOKEN_VALIDITY_MILLIS = 15L * 60L * 1000L
        val REFRESH_TOKEN_VALIDITY_MILLIS = 30L * 24L * 60L * 60L * 1000L
    }

}