package com.main.wave.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.main.wave.entity.Facility;
import com.main.wave.mapper.FacilityMapper;
import com.main.wave.service.FacilityService;
import org.springframework.stereotype.Service;

@Service
public class FacilityServiceImpl extends ServiceImpl<FacilityMapper, Facility> implements FacilityService {
}
