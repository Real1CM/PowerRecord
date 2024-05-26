package com.main.wave.enumclass;

import lombok.Getter;

@Getter
public enum ErrorMessageEnum {

    TOKEN_EXPIRED("无效token"),

    USER_NAME_EXISTS("用户名已存在"),

    FACILITY_NAME_EXISTS("设备名称已存在"),

    USER_NAME_OR_PASSWORD_ERROR("用户名或密码错误"),

    WAVE_DATA_COMPARE_ERROR("波形数据比对失败")
    ;

    private final String message;


    ErrorMessageEnum(String message) {
        this.message = message;
    }
}
