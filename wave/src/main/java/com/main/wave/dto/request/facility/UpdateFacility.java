package com.main.wave.dto.request.facility;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateFacility {
    private Long id;

    private String name;

    private String macAddress;
}
