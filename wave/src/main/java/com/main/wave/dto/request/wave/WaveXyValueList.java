package com.main.wave.dto.request.wave;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class WaveXyValueList {
    private List<WaveXyValue> value;
}
