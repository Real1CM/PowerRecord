package com.main.wave.dto.mapper;

import com.main.wave.dto.request.facility.AddFacility;
import com.main.wave.dto.request.facility.BindWaveData;
import com.main.wave.dto.request.facility.UpdateFacility;
import com.main.wave.entity.Facility;
import com.main.wave.entity.FacilityWave;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FacilityMapper {

    FacilityMapper INSTANCE = Mappers.getMapper(FacilityMapper.class);


    @Mapping(source = "name",target = "name")
    @Mapping(source = "macAddress",target = "macAddress")
    Facility addRequestToFacility(AddFacility facility);


    @Mapping(source = "name",target = "name")
    @Mapping(source = "macAddress",target = "macAddress")
    @Mapping(source = "id",target = "id")
    Facility updateRequestToFacility(UpdateFacility facility);
}
