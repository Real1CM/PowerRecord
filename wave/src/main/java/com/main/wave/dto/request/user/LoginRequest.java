package com.main.wave.dto.request.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String name;
    private String password;
}
