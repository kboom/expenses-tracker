package com.toptal.ggurgul.timezones.integration

import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.mail.MailSender
import javax.sql.DataSource


@Configuration
open class IntegrationTestContext {

    /**
     * Note that this makes sense only for simple usage of PostgreSQL database.
     * If the needs become more complex real database has to be started just like for functional tests.
     */
    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.h2.Driver")
        dataSource.url = "jdbc:h2:mem:db;MODE=PostgreSQL"
        dataSource.username = "sa"
        dataSource.password = ""
        return dataSource
    }

    @Bean
    @Primary
    fun mailSender(): MailSender {
        return Mockito.mock(MailSender::class.java);
    }

}