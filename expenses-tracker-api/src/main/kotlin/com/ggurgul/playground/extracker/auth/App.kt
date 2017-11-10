package com.ggurgul.playground.extracker.auth

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.cloud.netflix.zuul.filters.RouteLocator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.stereotype.Component


@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableOAuth2Sso
@Configuration
class App : WebSecurityConfigurerAdapter() {

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

    @Configuration
    class WebMvcConfig : WebMvcConfigurerAdapter() {

        override fun addViewControllers(registry: ViewControllerRegistry) {
            registry.addViewController("/").setViewName("forward:/index.html")
        }

        override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
            super.addResourceHandlers(registry)
            if (!registry.hasMappingForPattern("/webjars/**")) {
                registry.addResourceHandler("/webjars/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/")
            }
            if (!registry.hasMappingForPattern("/**")) {
                registry.addResourceHandler("/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/expenses-tracker-ui/6.0.0/")
            }
//            if (!registry.hasMappingForPattern("/**")) {
//                registry.addResourceHandler("/**")
//                        .addResourceLocations(*CLASSPATH_RESOURCE_LOCATIONS)
//            }
        }

    }

    @Component
    class CustomPathZuulFilter : ZuulFilter() {

        companion object {
            val REQUEST_URI_KEY = "requestURI"
        }

        override fun filterType(): String {
            return "pre"
        }

        override fun filterOrder(): Int {
            return PreDecorationFilter.FILTER_ORDER + 1
        }

        override fun shouldFilter(): Boolean {
            return true
        }

        override fun run(): Any? {
            val context = RequestContext.getCurrentContext()
            val originalRequestPath = context.get(REQUEST_URI_KEY)
            val modifiedRequestPath = "/api/" + originalRequestPath
            context.put(REQUEST_URI_KEY, modifiedRequestPath)
            return null
        }

    }

    override fun configure(http: HttpSecurity) {
        http
                .logout().logoutSuccessUrl("/").and()
                .authorizeRequests().antMatchers("/", "/logout", "/static/**", "/assets/**", "/webjars/**")
                .permitAll().anyRequest().authenticated().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}
