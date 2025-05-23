package com.helloIftekhar.springJwt.dto;

public record PasswordResetRequest(
        String email,
        String code,
        String newPassword
) {}