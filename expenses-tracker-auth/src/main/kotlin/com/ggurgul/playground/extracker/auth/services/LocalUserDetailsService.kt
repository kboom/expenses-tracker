package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.exceptions.UserNotFoundException
import com.ggurgul.playground.extracker.auth.management.SystemRunner
import com.ggurgul.playground.extracker.auth.models.IdentityType
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocalUserDetailsService
@Autowired constructor(
        private val userRepository: UserRepository,
        private val systemRunner: SystemRunner,
        @Qualifier("predefinedUsers") private val predefinedUsers: Map<String, UserPrincipal>
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String) = predefinedUsers.getOrElse(username) {
        systemRunner.runInSystemContext {
            userRepository.findByUsername(username).map { user -> UserPrincipalEntity(user) }
        }.orElseThrow { UserNotFoundException() }
    }

    fun findByIdentity(identity: String, identityType: IdentityType): Optional<UserPrincipalEntity> {
        return systemRunner.runInSystemContext {
            userRepository.findByIdentityAndType(identity, identityType).map { user -> UserPrincipalEntity(user) }
        }
    }

    fun findByEmail(mail: String): Optional<UserPrincipalEntity> {
        return systemRunner.runInSystemContext {
            userRepository.findByEmail(mail).map { user -> UserPrincipalEntity(user) }
        }
    }

}
