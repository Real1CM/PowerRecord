package com.main.wave.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.main.wave.entity.WaveData;
import com.main.wave.mapper.WaveDataMapper;
import com.main.wave.service.WaveDataService;
import org.springframework.stereotype.Service;

@Service
public class WaveDataServiceImpl extends ServiceImpl<WaveDataMapper, WaveData> implements WaveDataService {
}
