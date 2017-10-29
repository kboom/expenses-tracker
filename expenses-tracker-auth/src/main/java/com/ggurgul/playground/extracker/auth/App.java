package com.ggurgul.playground.extracker.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Implement internal user mechanisms https://gigsterous.github.io/engineering/2017/03/01/spring-boot-4.html
 *
 * http://www.swisspush.org/security/2016/10/17/oauth2-in-depth-introduction-for-enterprises
 */
@SpringBootApplication
@RestController
public class App {

    @RequestMapping(path = {"/user", "/me"}, method = GET,
            produces = { MediaType.ALL_VALUE })
    public Principal user(Principal principal) {
        return principal;
    }


    @Configuration
    public static class WebConfig extends WebMvcConfigurerAdapter {

        @Override
        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            configurer.favorPathExtension(false).
                    favorParameter(true).
                    defaultContentType(MediaType.APPLICATION_JSON).
                    mediaType("xml", MediaType.APPLICATION_XML);
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}


