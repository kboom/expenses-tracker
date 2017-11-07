package com.ggurgul.playground.extracker.auth.handlers

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LocalAuthenticationFailureHandler : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception)
        response.sendRedirect("/login?error=invalid_credentials")
//        request.getRequestDispatcher("/login?error=invalid_credentials").forward(request, response)
    }

}