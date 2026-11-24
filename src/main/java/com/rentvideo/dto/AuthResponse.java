package com.rentvideo.dto;

public record AuthResponse(
        String token,
        String tokenType,
        UserResponse user
) {
}