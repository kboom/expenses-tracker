package com.ggurgul.playground.extracker.auth

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.cloud.netflix.zuul.filters.RouteLocator
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableOAuth2Sso
@Configuration
class App {

    private val LOG = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun commandLineRunner(routeLocator: RouteLocator): CommandLineRunner =
            CommandLineRunner { args ->
                routeLocator.routes
                        .forEach { route ->
                            LOG.info("${route.id} (${route.location}) ${route.fullPath}")
                        }
            }

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowCredentials = true
            addAllowedOrigin("*")
            addAllowedHeader("*")
            addAllowedMethod("OPTIONS")
            addAllowedMethod("HEAD")
            addAllowedMethod("GET")
            addAllowedMethod("PUT")
            addAllowedMethod("POST")
            addAllowedMethod("DELETE")
            addAllowedMethod("PATCH")
        })
        return CorsFilter(source)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}
