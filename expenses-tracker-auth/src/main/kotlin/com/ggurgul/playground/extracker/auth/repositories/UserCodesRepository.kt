package com.ggurgul.playground.extracker.auth.repositories

import com.ggurgul.playground.extracker.auth.models.User
import com.ggurgul.playground.extracker.auth.models.UserCode
import com.ggurgul.playground.extracker.auth.models.UserCodeType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserCodesRepository : CrudRepository<UserCode, Long> {

    fun findByUserAndType(user: User, type: UserCodeType): Optional<UserCode>

    fun deleteByUser(user: User)

}