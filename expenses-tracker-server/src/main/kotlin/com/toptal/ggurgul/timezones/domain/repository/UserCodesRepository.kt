package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.models.security.UserCode
import com.toptal.ggurgul.timezones.domain.models.security.UserCodeType
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import springfox.documentation.annotations.ApiIgnore
import java.util.*

@ApiIgnore
@Repository
@RepositoryRestResource(exported = false)
interface UserCodesRepository : CrudRepository<UserCode, Long> {

    fun findByUserAndType(user: User, type: UserCodeType): Optional<UserCode>

    fun deleteByUser(user: User)

}