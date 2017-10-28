package com.toptal.ggurgul.timezones.security

import com.toptal.ggurgul.timezones.domain.models.security.Authority
import com.toptal.ggurgul.timezones.domain.models.security.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import kotlin.streams.toList

object JwtUserFactory {

    fun create(user: User): JwtUser {
        return JwtUser(
                user.id,
                user.username,
                user.email,
                user.password,
                mapToGrantedAuthorities(user.authorities),
                user.enabled!!,
                user.lastPasswordResetDate
        )
    }

    private fun mapToGrantedAuthorities(authorities: List<Authority>?): List<GrantedAuthority> {
        return authorities!!.stream()
                .map { authority -> SimpleGrantedAuthority(authority.name!!.name) }
                .toList()
    }

}
