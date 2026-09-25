package com.inspiredcoda.springdatajpah2.controller

import com.inspiredcoda.springdatajpah2.controller.model.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception

@RestControllerAdvice
class GlobalExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
        val errorBody = ErrorResponse(
            status = e.statusCode.value(),
            message = e.reason,
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity.status(e.statusCode).body(errorBody)
    }

    override fun handleErrorResponseException(
        ex: ErrorResponseException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorBody = ErrorResponse(
            status = ex.statusCode.value(),
            message = ex.message,
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity.status(status).body(errorBody)
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {

        val errorBody = ErrorResponse(
            status = statusCode.value(),
            message = ex.message,
            timestamp = System.currentTimeMillis()
        )

        return ResponseEntity.status(statusCode).body(errorBody)
    }

}