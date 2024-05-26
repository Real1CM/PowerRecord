package com.main.wave.dto;

import com.main.wave.enumclass.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BaseResponse<T> {
    private StatusEnum status;
    private T data;
    private String errorMessage;
}
