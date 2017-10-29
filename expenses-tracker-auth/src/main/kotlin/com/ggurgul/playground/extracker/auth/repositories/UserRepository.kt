package com.ggurgul.playground.extracker.auth.repositories

import com.ggurgul.playground.extracker.auth.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findOneByUsername(username: String): User

}
