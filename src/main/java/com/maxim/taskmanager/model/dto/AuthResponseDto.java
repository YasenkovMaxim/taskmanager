package com.maxim.taskmanager.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private Long expiresAt;

    private static final String TOKEN_TYPE = "Bearer";

    public AuthResponseDto(String accessToken, Long expiresAt) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
    }

    public String getTokenType() {
        return TOKEN_TYPE;
    }
}
