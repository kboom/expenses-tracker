package com.ggurgul.playground.extracker.auth.repositories

import com.ggurgul.playground.extracker.auth.models.IdentityType
import com.ggurgul.playground.extracker.auth.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    @Query("select w from User w where w in (select i.user from BoundIdentity i where i.identityType = :type and i.internalId = :identity)")
    fun findByIdentityAndType(
            @Param("identity") identity: String,
            @Param("type") identityType: IdentityType
    ): Optional<User>

}
