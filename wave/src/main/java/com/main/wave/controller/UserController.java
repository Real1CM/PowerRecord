package com.main.wave.controller;

import com.main.wave.biz.UserBizService;
import com.main.wave.configuration.IgnoreToken;
import com.main.wave.dto.BaseResponse;
import com.main.wave.dto.Response;
import com.main.wave.dto.request.user.LoginRequest;
import com.main.wave.dto.request.user.RegisterRequest;
import com.main.wave.dto.response.LoginResponse;
import com.main.wave.enumclass.ErrorMessageEnum;
import com.main.wave.exception.UnAuthorizeRequestException;
import com.main.wave.util.JWTUtils;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@IgnoreToken
public class UserController {

    @Autowired
    private UserBizService bizService;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        boolean login = bizService.login(request);
        if(login) {
            return Response.success(generateResponse(request.getName()));
        }
        return Response.error(ErrorMessageEnum.USER_NAME_OR_PASSWORD_ERROR);
    }



    @PutMapping("/register")
    public BaseResponse<Boolean> register(@RequestBody RegisterRequest request) {
        boolean register = bizService.register(request);
        return register ? Response.success() : Response.error(ErrorMessageEnum.USER_NAME_EXISTS);
    }


    @PostMapping("/refresh/token")
    public BaseResponse<LoginResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refresh-token");
        if(Strings.isBlank(refreshToken)) throw new UnAuthorizeRequestException();
        String userName = jwtUtils.extractUserName(refreshToken, true);
        Boolean b = jwtUtils.validateToken(refreshToken, userName,true);
        if(b) {
            LoginResponse loginResponse = generateResponse(userName);
            return Response.success(loginResponse);
        }
        throw new UnAuthorizeRequestException();
    }

    private LoginResponse generateResponse(String userName) {
        String token = jwtUtils.generateToken(userName);
        String refreshToken = jwtUtils.generateRefreshToken(userName);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setName(userName);
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken);
        return loginResponse;
    }


}
