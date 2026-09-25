package com.inspiredcoda.springdatajpah2.domain.service

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class HashEncoder {

    private val HASH_SALT = BCrypt.gensalt()

    /**
     * One-way hashing algorithm. Use this for your passwords andd tokens.
     * NOTE: Whatever you hash cannot be reversed to get the original value
    * */
    fun hash(value: String): String {
        return BCrypt.hashpw(value, HASH_SALT)
    }


}