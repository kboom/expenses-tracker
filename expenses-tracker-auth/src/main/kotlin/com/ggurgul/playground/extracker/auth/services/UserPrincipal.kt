package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.models.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal


class UserPrincipal(
        val user: User
) : UserDetails, Principal {

    override fun getAuthorities() = user.authorities
            .map { authority -> SimpleGrantedAuthority(authority.name!!.name) }
            .toList()

    override fun getName() = username

    override fun isEnabled() = user.enabled

    override fun getUsername(): String = user.username

    override fun getPassword() = user.password

    override fun isCredentialsNonExpired() = true

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

}