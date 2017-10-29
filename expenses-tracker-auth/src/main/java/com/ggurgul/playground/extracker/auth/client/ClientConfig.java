package com.ggurgul.playground.extracker.auth.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ClientConfig {

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    @ConfigurationProperties("github")
    public ClientResource github() {
        return new ClientResource();
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResource facebook() {
        return new ClientResource();
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResource google() {
        return new ClientResource();
    }

    public final Filter createClientFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(createClientFilter(facebook(), "/login/facebook"));
        filters.add(createClientFilter(google(), "/login/google"));
        filters.add(createClientFilter(github(), "/login/github"));
        filter.setFilters(filters);
        return filter;
    }

    private Filter createClientFilter(ClientResource client, String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
                path);
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
                client.getResource().getUserInfoUri(), client.getClient().getClientId());
        tokenServices.setRestTemplate(template);
        filter.setTokenServices(tokenServices);
        return filter;
    }

}
