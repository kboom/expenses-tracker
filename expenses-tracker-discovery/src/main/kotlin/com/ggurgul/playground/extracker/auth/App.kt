package com.ggurgul.playground.extracker.auth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard

@SpringBootApplication
@EnableEurekaServer
@EnableHystrixDashboard
class App

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}
