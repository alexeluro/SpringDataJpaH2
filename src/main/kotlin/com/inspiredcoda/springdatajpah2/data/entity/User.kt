package com.inspiredcoda.springdatajpah2.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.Collections
import java.util.UUID

@Entity
@Table(name = "users_entity")
data class User(
    @Id
    val id: UUID,
    val username: String,
    val email: String,
    val hashedPassword: String,
    val role: UserRole
)/*: UserDetails*/ {

//    override fun getAuthorities(): Collection<out GrantedAuthority> {
//        return Collections.emptyList<GrantedAuthority>()
//    }
//
//    override fun getPassword(): String? {
//        return hashedPassword
//    }
//
//    override fun getUsername(): String {
//        return username
//    }
    enum class UserRole: Serializable {
        USER, ADMIN
    }

}
