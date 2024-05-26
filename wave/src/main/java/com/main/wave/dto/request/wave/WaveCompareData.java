package com.main.wave.dto.request.wave;

import cn.hutool.core.annotation.Alias;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class WaveCompareData {

    @Alias("时间戳")
    private Date startTime;

    @Alias("振幅")
    private Integer value;


}
