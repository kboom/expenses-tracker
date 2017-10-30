package com.ggurgul.playground.extracker.auth.repositories

import com.ggurgul.playground.extracker.auth.models.Authority
import com.ggurgul.playground.extracker.auth.models.AuthorityName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository : JpaRepository<Authority, AuthorityName>