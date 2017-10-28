package com.toptal.ggurgul.timezones.domain.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UserCodeTranslatorTest {

    private var translator = UserCodeTranslator()

    @Test
    fun canGenerateCode() {
        assertThat(translator.generateFor("kate")).isNotEmpty()
    }

    @Test
    fun canReadCode() {
        assertThat(translator.readFrom("a2F0ZTo2ZTVmMjJkZTQ5YzMzOTkwMDEyM2IyMjlmYTAwNjAwNQ==")).startsWith("kate")
    }

}