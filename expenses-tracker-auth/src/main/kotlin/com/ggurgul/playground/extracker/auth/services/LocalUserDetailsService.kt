package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.exceptions.UserNotFoundException
import com.ggurgul.playground.extracker.auth.management.SystemRunner
import com.ggurgul.playground.extracker.auth.models.IdentityType
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LocalUserDetailsService
@Autowired constructor(
        private val userRepository: UserRepository,
        private val systemRunner: SystemRunner,
        @Qualifier("predefinedUsers") private val predefinedUsers: Map<String, UserPrincipal>
) : UserDetailsService {

    override fun loadUserByUsername(email: String) = predefinedUsers.getOrElse(email) {
        systemRunner.runInSystemContext {
            userRepository.findByEmail(email).map { user -> UserPrincipalEntity(user) }
        }.orElseThrow { UserNotFoundException() }!!
    }

    fun findByIdentity(identity: String, identityType: IdentityType) = systemRunner.runInSystemContext {
        userRepository.findByIdentityAndType(identity, identityType)
    }

    fun findByEmail(mail: String) = systemRunner.runInSystemContext {
        userRepository.findByEmail(mail)
    }


}
