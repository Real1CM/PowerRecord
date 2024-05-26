package com.main.wave.biz;

import com.main.wave.dto.request.user.LoginRequest;
import com.main.wave.dto.request.user.RegisterRequest;

public interface UserBizService {
    boolean login(LoginRequest request);

    boolean register(RegisterRequest request);
}
