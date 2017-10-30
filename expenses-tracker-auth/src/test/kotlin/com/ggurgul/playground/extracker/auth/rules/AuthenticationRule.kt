package com.ggurgul.playground.extracker.auth.rules

import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.*


abstract class AuthenticationRule(
        private val httpClient: HttpClient = HttpClients.createDefault()
) : TestRule {

    var token: String? = null

    abstract fun getPort(): Int

    override fun apply(base: Statement, description: Description) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            val authenticatedAsUserOptional = Optional.ofNullable(description.getAnnotation(AuthenticatedAsUser::class.java))

            if (authenticatedAsUserOptional.isPresent) {
                val authenticatedAsUser = authenticatedAsUserOptional.get()
                val postRequest = HttpPost("http://localhost:${getPort()}/oauth/token")
//                postRequest.setHeader("Accept", "application/json");
//                postRequest.setHeader("Content-type", "application/json");

                postRequest.entity = UrlEncodedFormEntity(mutableListOf(
                        BasicNameValuePair("grant_type", "password"),
                        BasicNameValuePair("client_id", CLIENT_ID),
                        BasicNameValuePair("username", authenticatedAsUser.user.username),
                        BasicNameValuePair("password", authenticatedAsUser.user.password)
                ))

                postRequest.setHeader("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("$CLIENT_ID:$CLIENT_SECRET".toByteArray()));

                val response = httpClient.execute(postRequest)
                if (response.statusLine.statusCode == 200) {
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

    companion object {
        private val CLIENT_ID = "expenses-tracker-service"
        private val CLIENT_SECRET = "expenses-tracker-service-secret"
    }

}