package com.ggurgul.playground.extracker.service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@SpringBootApplication
@EnableOAuth2Sso
@RestController
@EnableEurekaClient
class App {

    @RequestMapping("/")
    fun home(user: Principal): String {
        return "Hello " + user.name
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(App::class.java, *args)
        }
    }

}
