package com.inspiredcoda.springdatajpah2.domain.repository

import com.inspiredcoda.springdatajpah2.data.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TokenRepository: JpaRepository<RefreshToken, UUID> {

    fun findByUserId(userId: UUID): RefreshToken?

    fun findByUserIdAndHashedToken(userId: UUID, hashedToken: String): RefreshToken?

}