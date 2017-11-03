package com.ggurgul.playground.extracker.auth.services

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ggurgul.playground.extracker.auth.models.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal


interface UserPrincipal : UserDetails, Principal {

    fun id(): Long?

    fun getFirstName(): String?

    fun getLastName(): String?

}

class UserPrincipalModel(
        @get:JsonIgnore
        val user: org.springframework.security.core.userdetails.User
) : UserPrincipal {

    override fun id(): Long? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFirstName() = null

    override fun getLastName() = null

    override fun getAuthorities() = user.authorities

    override fun isEnabled() = user.isEnabled

    override fun getUsername() = user.username

    override fun isCredentialsNonExpired() = user.isCredentialsNonExpired

    @JsonIgnore
    override fun getPassword() = user.password

    override fun isAccountNonExpired() = user.isAccountNonExpired

    override fun isAccountNonLocked() = user.isAccountNonLocked

    override fun getName() = user.username

}

class UserPrincipalEntity(
        @get:JsonIgnore
        val user: User
) : UserPrincipal {

    override fun id(): Long? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFirstName() = user.firstName

    override fun getLastName() = user.lastName

    override fun getAuthorities() = user.authorities
            .map { authority -> SimpleGrantedAuthority(authority.name!!.name) }
            .toList()

    override fun getName() = username

    override fun isEnabled() = user.enabled

    override fun getUsername(): String = user.email

    @JsonIgnore
    override fun getPassword() = user.password

    override fun isCredentialsNonExpired() = true

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

}