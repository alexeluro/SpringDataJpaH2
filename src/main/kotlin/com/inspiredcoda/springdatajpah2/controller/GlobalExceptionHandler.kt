package com.inspiredcoda.springdatajpah2.controller

import com.inspiredcoda.springdatajpah2.controller.model.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
        val errorBody = ErrorResponse(
            status = e.statusCode.value(),
            message = e.reason,
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity.status(e.statusCode).body(errorBody)
    }

}