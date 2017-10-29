package com.ggurgul.playground.extracker.auth.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
class User(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "user_id", nullable = false, updatable = false)
        private val id: Long? = null,

        @Column(name = "username", nullable = false, unique = true)
        private val username: String? = null,

        @Column(name = "password", nullable = false)
        private val password: String? = null,

        @Column(name = "enabled", nullable = false)
        private val enabled: Boolean = false

) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return ArrayList() // todo for now
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String? {
        return username
    }

    companion object {
        private val serialVersionUID = 1L
    }

}