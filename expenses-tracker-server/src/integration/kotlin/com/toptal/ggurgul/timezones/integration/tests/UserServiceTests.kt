package com.toptal.ggurgul.timezones.integration.tests

import com.toptal.ggurgul.timezones.domain.services.UserService
import com.toptal.ggurgul.timezones.integration.AbstractIntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTests : AbstractIntegrationTest() {

    @Autowired
    var userService: UserService? = null

    @Test
    fun canCreateUser() {

    }

}