package com.toptal.ggurgul.timezones.exceptions.advice

import com.ggurgul.playground.extracker.auth.exceptions.ApplicationErrorMessage
import org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
internal class ExceptionHandlerAdvice {

    @ExceptionHandler
    @ResponseBody
    @Throws(Exception::class)
    fun handle(exception: Exception): ResponseEntity<ApplicationErrorMessage> {
        val annotation = findMergedAnnotation(exception.javaClass, ResponseStatus::class.java) ?: throw exception
        val responseStatus = annotation.code
        val body = ApplicationErrorMessage(responseStatus.value(), annotation.reason)
        return ResponseEntity(body, responseStatus)
    }

}