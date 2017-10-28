package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.security.Authority
import com.toptal.ggurgul.timezones.domain.models.security.AuthorityName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository
@RepositoryRestResource
interface AuthorityRepository : JpaRepository<Authority, AuthorityName>