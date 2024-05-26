package com.main.wave.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;

    private String password;
}
