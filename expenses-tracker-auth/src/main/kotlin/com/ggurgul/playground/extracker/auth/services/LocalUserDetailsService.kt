package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.exceptions.UserNotFoundException
import com.ggurgul.playground.extracker.auth.management.SystemRunner
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class LocalUserDetailsService
@Autowired constructor(
        private val userRepository: UserRepository,
        private val systemRunner: SystemRunner
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return systemRunner.runInSystemContext {
            userRepository.findByUsername(username).map { user -> SecurityUserAdapter(user) }
        }.orElseThrow { UserNotFoundException() }
    }

}
