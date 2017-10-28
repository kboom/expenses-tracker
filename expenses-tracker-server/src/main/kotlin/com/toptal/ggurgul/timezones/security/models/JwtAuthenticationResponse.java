package com.toptal.ggurgul.timezones.security.models;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "AuthenticationResponse", description = "A response with token for the authenticated user")
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
