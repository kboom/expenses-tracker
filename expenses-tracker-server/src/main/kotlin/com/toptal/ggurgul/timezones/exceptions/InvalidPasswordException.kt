package com.toptal.ggurgul.timezones.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid password provided")
class InvalidPasswordException : RuntimeException() {
}