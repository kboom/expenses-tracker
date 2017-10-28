package com.toptal.ggurgul.timezones.security.permissions.checkers

import com.toptal.ggurgul.timezones.domain.models.Timezone
import com.toptal.ggurgul.timezones.domain.repository.TimezoneRepository
import com.toptal.ggurgul.timezones.security.SystemRunner
import com.toptal.ggurgul.timezones.security.permissions.Permission.*
import com.toptal.ggurgul.timezones.security.permissions.PermissionChecker
import com.toptal.ggurgul.timezones.security.permissions.PermissionGrantRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class TimezonePermissionChecker
@Autowired constructor(
        private val timezoneRepository: TimezoneRepository,
        private val systemRunner: SystemRunner
) : PermissionChecker {

    companion object {
        val handledPermissions = setOf(CREATE_TIMEZONE, VIEW_TIMEZONE, EDIT_TIMEZONE, DELETE_TIMEZONE)
    }

    override fun shouldGrantPermissionFor(grantRequest: PermissionGrantRequest): Boolean {
        return if (grantRequest.permissionNeeded == CREATE_TIMEZONE) {
            true
        } else {
            val timezone = systemRunner.runInSystemContext { timezoneRepository.findOne(getIdFrom(grantRequest)) }
            timezone.owner!!.id == grantRequest.principal.id
        }
    }

    override fun canHandle(grantRequest: PermissionGrantRequest) =
            handledPermissions.contains(grantRequest.permissionNeeded)

    private fun getIdFrom(grantRequest: PermissionGrantRequest) =
            if (grantRequest.targetId != null) grantRequest.targetId as Long
            else (grantRequest.targetObject!! as Timezone).id!!

}