package com.ggurgul.playground.extracker.auth.management

import com.fasterxml.jackson.databind.ObjectMapper
import com.ggurgul.playground.extracker.auth.models.User
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.TestComponent
import org.springframework.context.ApplicationListener
import java.util.*

@TestComponent
class LoginManager : ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private var appPort: Int = 8080

    private val httpClient: HttpClient = HttpClients.createDefault()

    fun getTokenFor(username: String, password: String): String {
        val response = httpClient.execute(HttpPost("http://localhost:$appPort/oauth/token").apply {
            setHeader("Authorization", "Basic " + Base64.getEncoder()
                    .encodeToString("$CLIENT_ID:$CLIENT_SECRET".toByteArray()));

            entity = UrlEncodedFormEntity(mutableListOf(
                    BasicNameValuePair("grant_type", "password"),
                    BasicNameValuePair("client_id", CLIENT_ID),
                    BasicNameValuePair("username", username),
                    BasicNameValuePair("password", password)
            ))
        })

        if (response.statusLine.statusCode == 200) {
            return ObjectMapper().readTree(EntityUtils.toByteArray(response.entity)).get("access_token").asText()
        } else {
            throw IllegalStateException("Could not log-in")
        }
    }

    override fun onApplicationEvent(evt: EmbeddedServletContainerInitializedEvent?) {
        appPort = evt!!.applicationContext.embeddedServletContainer.port
    }

    companion object {
        private val CLIENT_ID = "expenses-tracker-service"
        private val CLIENT_SECRET = "expenses-tracker-service-secret"
    }

}