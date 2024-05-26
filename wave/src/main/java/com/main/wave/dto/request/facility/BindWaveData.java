package com.main.wave.dto.request.facility;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BindWaveData {
    private List<Long> waveDataIds;

    private Long facilityId;
}
