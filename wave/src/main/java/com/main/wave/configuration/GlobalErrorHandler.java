package com.main.wave.configuration;

import com.main.wave.dto.BaseResponse;
import com.main.wave.dto.Response;
import com.main.wave.enumclass.ErrorMessageEnum;
import com.main.wave.exception.IllegalRequestException;
import com.main.wave.exception.UnAuthorizeRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalErrorHandler {


    @ExceptionHandler(IllegalRequestException.class)
    @ResponseBody
    public BaseResponse<String> illegalRequest(HttpServletRequest request,Exception exception) {
        return Response.error(exception.getMessage());
    }


    @ExceptionHandler({UnAuthorizeRequestException.class, ExpiredJwtException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> unAuthorizeRequest(HttpServletRequest request,Exception exception) {
        return Response.error(ErrorMessageEnum.TOKEN_EXPIRED);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BaseResponse<String> otherException(HttpServletRequest request,Exception exception) {
        return Response.error(exception.getMessage());
    }

}
