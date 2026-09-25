package com.inspiredcoda.springdatajpah2.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "users_entity")
data class User(
    @Id
    val id: UUID,

    @Column(name = "username")
    private val username: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "hashed_password")
    val hashedPassword: String,

    @Column(name = "role")
    val role: UserRole
): UserDetails {

    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_$role"))
    }

    override fun getPassword(): String? {
        return hashedPassword
    }

    override fun getUsername(): String {
        return username
    }


    enum class UserRole : Serializable {
        USER, ADMIN
    }

}
