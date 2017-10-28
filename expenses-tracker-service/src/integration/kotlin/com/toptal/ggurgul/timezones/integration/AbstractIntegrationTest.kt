package com.toptal.ggurgul.timezones.integration

import com.toptal.ggurgul.timezones.TimezonesApplication
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@Category(IntegrationTest::class)
@ContextConfiguration(classes = arrayOf(IntegrationTestContext::class))
@SpringBootTest(classes = arrayOf(TimezonesApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = arrayOf("classpath:application-test.yml"))
open class AbstractIntegrationTest