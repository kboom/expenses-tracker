package com.toptal.ggurgul.timezones.functional.rules

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.*


class AuthenticationRule(
        private val httpClient: HttpClient = HttpClients.createDefault()
) : TestRule {

    var token: String? = null

    override fun apply(base: Statement, description: Description) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            val authenticatedAsUserOptional = Optional.ofNullable(description.getAnnotation(AuthenticatedAsUser::class.java))

            if (authenticatedAsUserOptional.isPresent) {
                val authenticatedAsUser = authenticatedAsUserOptional.get()
                val postRequest = HttpPost("http://localhost:8080/api/auth")
                postRequest.setHeader("Accept", "application/json");
                postRequest.setHeader("Content-type", "application/json");
                postRequest.entity = StringEntity("""{
                        "username": "${authenticatedAsUser.user.username}",
                        "password": "${authenticatedAsUser.user.password}"
                    }""".trimIndent())
                val response = httpClient.execute(postRequest)
                if(response.statusLine.statusCode == 200) {
                    val responseString = EntityUtils.toString(response.entity)
                    token = responseString
                            .replace("{\"token\":\"", "")
                            .replace("\"}", "")
                } else {
                    throw IllegalStateException("Could not log-in")
                }
            }
            base.evaluate()
            token = null
        }
    }

}