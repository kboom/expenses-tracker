package com.toptal.ggurgul.timezones.security

import com.toptal.ggurgul.timezones.domain.models.security.AuthorityName
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component

@Component
open class SystemRunner {

    fun <T> runInSystemContext(action: () -> T) : T {
        val currentAuthentication = SecurityContextHolder.getContext().authentication

        val authentication = PreAuthenticatedAuthenticationToken(SystemUser.get(), "",
                mutableListOf(SimpleGrantedAuthority(AuthorityName.ROLE_SYSTEM.name)))
        SecurityContextHolder.getContext().authentication = authentication

        val result = action()

        SecurityContextHolder.getContext().authentication = currentAuthentication

        return result
    }

}