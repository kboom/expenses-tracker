package com.toptal.ggurgul.timezones.integration.tests

import com.toptal.ggurgul.timezones.integration.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder


open class UserCreationTests : AbstractIntegrationTest() {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun encodesPassword() {
        assertThat(passwordEncoder.encode("qwerty666")).isNotNull()
    }

}