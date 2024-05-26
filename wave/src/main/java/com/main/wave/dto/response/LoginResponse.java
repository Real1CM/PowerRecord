package com.main.wave.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String name;
    private String token;
    private String refreshToken;
}
