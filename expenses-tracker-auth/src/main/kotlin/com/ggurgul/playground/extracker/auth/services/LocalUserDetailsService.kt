package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.exceptions.UserNotFoundException
import com.ggurgul.playground.extracker.auth.management.SystemRunner
import com.ggurgul.playground.extracker.auth.models.IdentityType
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocalUserDetailsService
@Autowired constructor(
        private val userRepository: UserRepository,
        private val systemRunner: SystemRunner
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserPrincipal {
        return systemRunner.runInSystemContext {
            userRepository.findByUsername(username).map { user -> UserPrincipal(user) }
        }.orElseThrow { UserNotFoundException() }
    }

    fun findByIdentity(identity: String, identityType: IdentityType): Optional<UserPrincipal> {
        return systemRunner.runInSystemContext {
            userRepository.findByIdentityAndType(identity, identityType).map { user -> UserPrincipal(user) }
        }
    }

    fun findByEmail(mail: String): Optional<UserPrincipal> {
        return systemRunner.runInSystemContext {
            userRepository.findByEmail(mail).map { user -> UserPrincipal(user) }
        }
    }

}
