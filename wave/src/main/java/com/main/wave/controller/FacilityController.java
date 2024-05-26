package com.main.wave.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.main.wave.dto.BaseResponse;
import com.main.wave.dto.Response;
import com.main.wave.dto.mapper.FacilityMapper;
import com.main.wave.dto.request.facility.AddFacility;
import com.main.wave.dto.request.facility.BindWaveData;
import com.main.wave.dto.request.facility.UpdateFacility;
import com.main.wave.entity.Facility;
import com.main.wave.entity.FacilityWave;
import com.main.wave.enumclass.ErrorMessageEnum;
import com.main.wave.service.FacilityService;
import com.main.wave.service.FacilityWaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/facility")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private FacilityWaveService facilityWaveService;

    @GetMapping("/list")
    public BaseResponse<List<Facility>> list() {
        List<Facility> list = facilityService.list();
        return Response.success(list);
    }




    @GetMapping("/page/{pageNumber}")
    public BaseResponse<Page<Facility>> facilityPage(@PathVariable Long pageNumber) {
        Page<Facility> page = facilityService.page(new Page<>(pageNumber, 10));
        return Response.success(page);
    }


    @PostMapping("/add")
    public BaseResponse<Boolean> addFacility(@RequestBody AddFacility addFacility) {
        LambdaQueryWrapper<Facility> query = new QueryWrapper<Facility>().lambda()
                .eq(Facility::getName, addFacility.getName());
        boolean exists = facilityService.exists(query);
        if(exists) return Response.error(ErrorMessageEnum.FACILITY_NAME_EXISTS);
        Facility facility = FacilityMapper.INSTANCE.addRequestToFacility(addFacility);
        boolean save = facilityService.save(facility);
        return save ? Response.success() : Response.error();
    }



    @PostMapping("/update")
    public BaseResponse<Boolean> updateFacility(@RequestBody UpdateFacility updateFacility) {
        Facility facility = FacilityMapper.INSTANCE.updateRequestToFacility(updateFacility);
        boolean update = facilityService.updateById(facility);
        return update ? Response.success() : Response.error();
    }


    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteFacility(@PathVariable Long id) {
        boolean delete = facilityService.removeById(id);
        return delete ? Response.success() : Response.error();
    }


    @PostMapping("/bind")
    public BaseResponse<Boolean> bindWaveData(@RequestBody BindWaveData bindWaveData) {
        List<Long> waveDataIds = bindWaveData.getWaveDataIds();
        List<FacilityWave> collect = waveDataIds.stream()
                .map(waveDataId -> {
                    FacilityWave wave = new FacilityWave();
                    wave.setWaveDataId(waveDataId);
                    wave.setFacilityId(bindWaveData.getFacilityId());
                    return wave;
                }).collect(Collectors.toList());
        boolean save = facilityWaveService.saveBatch(collect);
        return save ? Response.success() : Response.error();
    }

    @PostMapping("/unbind")
    public BaseResponse<Boolean> unBindWaveData(@RequestParam("facilityId") Long facilityId) {
        LambdaQueryWrapper<FacilityWave> removeQuery = new QueryWrapper<FacilityWave>()
                .lambda().eq(FacilityWave::getFacilityId, facilityId);
        boolean save = facilityWaveService.remove(removeQuery);
        return save ? Response.success() : Response.error();
    }

}
