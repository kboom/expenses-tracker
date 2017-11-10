package com.ggurgul.playground.extracker.auth.management

import com.ggurgul.playground.extracker.auth.models.Authority
import com.ggurgul.playground.extracker.auth.models.AuthorityName
import com.ggurgul.playground.extracker.auth.models.User
import com.ggurgul.playground.extracker.auth.repositories.AuthorityRepository
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import org.springframework.security.crypto.password.PasswordEncoder

@TestComponent
class UsersManager {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authorityRepository: AuthorityRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    fun createDummyUser() = userRepository.save(User(
            email = "someone@anyone.com",
            password = passwordEncoder.encode("secret"),
            enabled = true,
            authorities = mutableListOf(Authority(AuthorityName.ROLE_USER))
    ))!!

    fun resetAll() {
        userRepository.deleteAll()
        authorityRepository.deleteAll()
        authorityRepository.save(Authority(AuthorityName.ROLE_USER))
    }

}