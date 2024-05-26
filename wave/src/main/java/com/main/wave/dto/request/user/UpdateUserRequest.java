package com.main.wave.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequest {

    private Long id;

    private String name;

    private String password;
}
