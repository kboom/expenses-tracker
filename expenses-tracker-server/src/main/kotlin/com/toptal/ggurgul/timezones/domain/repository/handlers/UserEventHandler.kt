package com.toptal.ggurgul.timezones.domain.repository.handlers

import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.repository.AuthorityRepository
import com.toptal.ggurgul.timezones.domain.repository.TimezoneRepository
import com.toptal.ggurgul.timezones.domain.repository.UserCodesRepository
import com.toptal.ggurgul.timezones.domain.repository.UserRepository
import com.toptal.ggurgul.timezones.security.SystemRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeDelete
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import javax.transaction.Transactional


@Component
@RepositoryEventHandler(User::class)
open class UserEventHandler
@Autowired constructor(
        private val passwordEncoder: BCryptPasswordEncoder,
        private val userRepository: UserRepository,
        private val authorityRepository: AuthorityRepository,
        private val systemRunner: SystemRunner,
        private val userCodesRepository: UserCodesRepository,
        private val timezoneRepository: TimezoneRepository
) {

    @HandleBeforeCreate
    fun handleUserCreate(user: User) {
        user.password = passwordEncoder.encode(user.password)
        user.authorities = systemRunner.runInSystemContext {
            authorityRepository.findAll(user.authorities!!.map { it.name })
        }
    }

    @HandleBeforeSave
    fun handleUserUpdate(user: User) {
        if (user.password != user.oldPassword) {
            user.password = passwordEncoder.encode(user.password)
        }
    }

    @Transactional
    @HandleBeforeDelete
    fun handleBeforeDelete(user: User) {
        userCodesRepository.deleteByUser(user)
        timezoneRepository.deleteByOwner(user)
    }

}