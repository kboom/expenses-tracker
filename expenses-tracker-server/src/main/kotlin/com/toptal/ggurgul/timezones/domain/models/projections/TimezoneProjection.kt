package com.toptal.ggurgul.timezones.domain.models.projections

import com.toptal.ggurgul.timezones.domain.models.Timezone
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.rest.core.config.Projection

@Projection(name = "withDetails", types = arrayOf(Timezone::class))
interface TimezoneProjection {

    fun getName(): String

    fun getLocationName(): String

    fun getDifferenceToGMT(): Int

    @Value("#{target.owner.username}")
    fun getOwner(): String

}