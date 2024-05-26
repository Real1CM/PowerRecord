package com.main.wave.configuration;

import com.main.wave.exception.IllegalRequestException;
import com.main.wave.exception.UnAuthorizeRequestException;
import com.main.wave.service.UserService;
import com.main.wave.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        IgnoreToken annotation = handlerMethod.getBeanType().getAnnotation(IgnoreToken.class);
        if(Objects.isNull(annotation)){
            annotation = handlerMethod.getMethodAnnotation(IgnoreToken.class);
        }
        if(Objects.nonNull(annotation))
            return true;
        String token = request.getHeader("token");
        String userName = request.getHeader("username");

        if(Objects.isNull(token) || Objects.isNull(userName)) {
            throw new UnAuthorizeRequestException();
        }

        JWTUtils jwtUtils = new JWTUtils();
        Boolean b = jwtUtils.validateToken(token, userName,false);
        if(!b) {
            throw new UnAuthorizeRequestException();
        }
        return true;
    }
}
