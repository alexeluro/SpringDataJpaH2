package com.inspiredcoda.springdatajpah2.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(
    @Id
    val userId: UUID, // The owner of the token

    @Column(name = "hashed_token")
    val hashedToken: String,

    @Column(name = "expires_in")
    val expiresIn: Instant,

    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()
)
