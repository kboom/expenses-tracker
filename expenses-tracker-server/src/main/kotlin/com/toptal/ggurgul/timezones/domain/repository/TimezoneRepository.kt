package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.Timezone
import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.Api
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.security.access.prepost.PreAuthorize
import javax.validation.Valid

// org.springframework.data.rest.webmvc.RepositoryEntityController
@Api(value = "timezone", description = "Timezone operations", tags = arrayOf("timezone"))
@RepositoryRestResource
interface TimezoneRepository : CrudRepository<Timezone, Long> {

    @RestResource(exported = false)
    fun deleteByOwner(user: User)

    // Methods used by Spring-Data-Rest

    @Query("select t from Timezone t where t.owner.username like ?#{hasRole('ROLE_ADMIN') ? '%' : principal.username}")
    override fun findAll(): MutableIterable<Timezone>

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_ADMIN') or hasPermission(#timezoneId, 'Timezone', 'timezone:create') or hasPermission(#timezone, 'Timezone', 'timezone:edit')")
    override fun <S : Timezone?> save(@Param("timezone") @Valid timezone: S?): S

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_ADMIN') or hasPermission(#timezoneId, 'Timezone', 'timezone:view')")
    override fun findOne(@Param("timezoneId") timezoneId: Long?): Timezone

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_ADMIN') or hasPermission(#timezone, 'timezone:delete')")
    override fun delete(@Param("timezone") timezone: Timezone?)


    // Other methods should also be secured but are not used (by default) by Spring-Data-Rest

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_ADMIN') or hasPermission(#timezoneId, 'Timezone', 'timezone:view')")
    override fun findAll(@Param("timezoneIds") timezoneIds: MutableIterable<Long>?): MutableIterable<Timezone>

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_ADMIN') or hasPermission(#timezoneId, 'Timezone', 'timezone:delete')")
    override fun delete(@Param("timezoneId") timezoneId: Long)

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_ADMIN') or hasPermission(#timezoneIds, 'Timezone', 'timezone:delete')")
    override fun delete(@Param("timezoneIds") timezoneIds: MutableIterable<Timezone>)

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_ADMIN')")
    override fun deleteAll()

}
