package com.toptal.ggurgul.timezones.domain.repository.handlers

import com.toptal.ggurgul.timezones.domain.models.Timezone
import com.toptal.ggurgul.timezones.domain.repository.TimezoneRepository
import com.toptal.ggurgul.timezones.domain.services.UserService
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component


@Component
@RepositoryEventHandler(Timezone::class)
open class TimezoneEventHandler
@Autowired constructor(
        private val userService: UserService,
        private val timezoneRepository: TimezoneRepository
) {

    companion object {
        private val LOG = LogFactory.getLog(TimezoneEventHandler::class.java)
    }

    @HandleBeforeCreate
    fun handleTimezoneSave(timezone: Timezone) {
        LOG.debug("Creating timezone")
        timezone.owner = userService.getActingUser()
    }

    @HandleBeforeSave
    fun handleTimezoneUpdate(timezone: Timezone) {
        LOG.debug("Updating timezone")
        val existingTimezone = timezoneRepository.findOne(timezone.id!!)
        timezone.owner = existingTimezone.owner
    }

}