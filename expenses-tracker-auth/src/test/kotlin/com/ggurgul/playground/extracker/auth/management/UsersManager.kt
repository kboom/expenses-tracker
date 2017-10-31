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
            username = "dummy",
            password = passwordEncoder.encode("secret"),
            email = "someone@anything.com",
            enabled = true,
            authorities = mutableListOf(Authority(AuthorityName.ROLE_USER))
    ))!!

    fun resetAll() {
        authorityRepository.deleteAll()
        userRepository.deleteAll()
        authorityRepository.save(Authority(AuthorityName.ROLE_USER))
    }

}