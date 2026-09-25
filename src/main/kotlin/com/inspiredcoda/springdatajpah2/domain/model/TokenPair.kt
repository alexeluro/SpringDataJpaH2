package com.inspiredcoda.springdatajpah2.domain.model

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)