package com.toptal.ggurgul.timezones.security.permissions

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary
@Component
open class CompositePermissionChecker(
        private val delegateCheckers: List<PermissionChecker>
) : PermissionChecker {

    override fun shouldGrantPermissionFor(grantRequest: PermissionGrantRequest) =
            delegateCheckers.first { it.canHandle(grantRequest) }
                    .shouldGrantPermissionFor(grantRequest)

    override fun canHandle(grantRequest: PermissionGrantRequest) =
            delegateCheckers.any { it.canHandle(grantRequest) }

}