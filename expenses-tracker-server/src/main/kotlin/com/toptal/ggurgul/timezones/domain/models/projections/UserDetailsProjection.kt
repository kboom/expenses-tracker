package com.toptal.ggurgul.timezones.domain.models.projections

import com.toptal.ggurgul.timezones.domain.models.security.Authority
import com.toptal.ggurgul.timezones.domain.models.security.User
import org.springframework.data.rest.core.config.Projection

@Projection(name = "withDetails", types = arrayOf(User::class))
interface UserDetailsProjection {

    fun getUsername(): String
    fun getEnabled(): Boolean
    fun getEmail(): String
    fun getFirstName(): String
    fun getLastName(): String
    fun getAuthorities(): List<Authority>

}