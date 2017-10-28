package com.ggurgul.playground.extracker.service.rules

import com.ninja_squad.dbsetup.DbSetupTracker
import com.ninja_squad.dbsetup.destination.DriverManagerDestination
import com.ninja_squad.dbsetup_kotlin.DbSetupBuilder
import com.ninja_squad.dbsetup_kotlin.dbSetup
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.*

class DataLoadingRule(
        private val configure: DbSetupBuilder.() -> Unit
) : TestRule {

    private val dbSetupTracker = DbSetupTracker()

    fun prepareDatabase(configure: DbSetupBuilder.() -> Unit) {
        val url = System.getProperty("db.url", "jdbc:postgresql://localhost:5432/test")
        val user = System.getProperty("db.user", "test")
        val password = System.getProperty("db.password", "test")

        dbSetupTracker.launchIfNecessary(dbSetup(to = DriverManagerDestination(url, user, password)) {
            deleteAllFrom("EXPENSE")
            configure()
        })
    }

    override fun apply(base: Statement, description: Description) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            prepareDatabase(configure);
            val readOnlyAnnotation = Optional.ofNullable(description.getAnnotation(ReadOnly::class.java))
            if (readOnlyAnnotation.isPresent) {
                dbSetupTracker.skipNextLaunch();
            }
            base.evaluate()
        }
    }

}