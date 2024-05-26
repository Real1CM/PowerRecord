package com.main.wave.dto;

import com.main.wave.enumclass.ErrorMessageEnum;
import com.main.wave.enumclass.StatusEnum;
import org.yaml.snakeyaml.util.EnumUtils;

public class Response {

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(StatusEnum.SUCCESS,null,null);
    }
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(StatusEnum.SUCCESS,data,null);
    }


    public static <T> BaseResponse<T> error(T data) {
        return new BaseResponse<>(StatusEnum.ERROR,data,null);
    }

    public static <T> BaseResponse<T> error(T data,String errorMessage) {
        return new BaseResponse<>(StatusEnum.ERROR,data,errorMessage);
    }

    public static <T> BaseResponse<T> error(ErrorMessageEnum errorMessage) {
        return new BaseResponse<>(StatusEnum.ERROR,null,errorMessage.getMessage());
    }


    public static <T> BaseResponse<T> error() {
        return new BaseResponse<>(StatusEnum.ERROR, null,null);
    }
}
