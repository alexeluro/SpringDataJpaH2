package com.inspiredcoda.springdatajpah2.controller.model

data class ErrorResponse(
    val success: Boolean = false,
    val status: Int,
    val message: String?,
    val timestamp: Long = System.currentTimeMillis()
)
