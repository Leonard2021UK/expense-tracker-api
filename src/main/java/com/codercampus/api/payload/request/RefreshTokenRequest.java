package com.codercampus.api.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * Extracts the refresh token from the request
 */
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
