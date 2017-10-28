package com.toptal.ggurgul.timezones.docs

import com.google.common.base.Predicates.or
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMethod
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.ApiKeyVehicle
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger2.annotations.EnableSwagger2

//https://jverhoelen.github.io/spring-rest-documentation-swagger-ui/
@Profile("docs")
@Configuration
@EnableSwagger2
@Import(SpringDataRestConfiguration::class, BeanValidatorPluginsConfiguration::class)
open class SwaggerConfig {

    companion object {
        val errorModelRef = ModelRef("Error")

        val defaultResponses = mutableListOf(
                ResponseMessageBuilder()
                        .code(500)
                        .message("Server error")
                        .responseModel(errorModelRef)
                        .build(),
                ResponseMessageBuilder()
                        .code(400)
                        .message("Bad request – wrong usage of the API")
                        .responseModel(errorModelRef)
                        .build(),
                ResponseMessageBuilder()
                        .code(401)
                        .message("No or invalid authentication")
                        .responseModel(errorModelRef)
                        .build(),
                ResponseMessageBuilder()
                        .code(403)
                        .message("Not permitted to access for users role")
                        .responseModel(errorModelRef)
                        .build(),
                ResponseMessageBuilder()
                        .code(404)
                        .message("Requested resource not available (anymore)")
                        .responseModel(errorModelRef)
                        .build()
        )
    }

    @Bean
    open fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, defaultResponses)
                .globalResponseMessage(RequestMethod.POST, defaultResponses)
                .globalResponseMessage(RequestMethod.PUT, defaultResponses)
                .globalResponseMessage(RequestMethod.DELETE, defaultResponses)
                .globalResponseMessage(RequestMethod.PATCH, defaultResponses)
                .select()
                .apis(or(RequestHandlerSelectors.any()))
                .paths(or(
                        PathSelectors.ant("/health/**"),
                        PathSelectors.ant("/auth/**"),
                        PathSelectors.ant("/account/**"),
                        PathSelectors.ant("/users/**"),
                        PathSelectors.ant("/timezones/**")
                ))
//                .paths(or(
//                        regex("/account(.*)"),
//                        regex("/user(.*)"),
//                        regex("/auth"),
//                        regex("/auth/(.*)"),
//                        regex("/timezones(.*)")
//                ))
                .build()
                .genericModelSubstitutes(ResponseEntity::class.java)
                .securitySchemes(arrayListOf(apiKey()))
                .securityContexts(arrayListOf(securityContext()))
    }

    @Bean
    open fun swaggerSecurity(): SecurityConfiguration {
        return SecurityConfiguration(
                null,
                null,
                null,
                null,
                "",
                ApiKeyVehicle.HEADER,
                "Authorization",
                ",")
    }

    private fun apiKey(): ApiKey {
        return ApiKey("Bearer", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(or(
                        PathSelectors.regex("/profile*"),
                        PathSelectors.regex("/timezones*")
                ))
                .build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        return arrayListOf(
                SecurityReference("Bearer", arrayOf(
                        AuthorizationScope("global", "accessEverything"))
                )
        )
    }

}