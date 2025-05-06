package com.helloIftekhar.springJwt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("message")
    private String message;

    @JsonProperty("role")
    private String role;

    public AuthenticationResponse(String accessToken, String refreshToken, String message,String mm) {
        this.accessToken = accessToken;
        this.message = message;
        this.refreshToken = refreshToken;
        this.role=mm;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }
}
