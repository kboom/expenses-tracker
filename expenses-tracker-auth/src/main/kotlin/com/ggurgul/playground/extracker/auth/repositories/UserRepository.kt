package com.ggurgul.playground.extracker.auth.repositories

import com.ggurgul.playground.extracker.auth.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    fun findByUsername(@Param("username") username: String): Optional<User>

}
