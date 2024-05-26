package com.main.wave.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.main.wave.dto.BaseResponse;
import com.main.wave.dto.Response;
import com.main.wave.dto.request.wave.WaveCompareData;
import com.main.wave.dto.request.wave.WaveXyValue;
import com.main.wave.dto.request.wave.WaveXyValueList;
import com.main.wave.entity.FacilityWave;
import com.main.wave.entity.WaveData;
import com.main.wave.enumclass.ErrorMessageEnum;
import com.main.wave.service.FacilityWaveService;
import com.main.wave.service.WaveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wave/data")
public class WaveDataController {

    @Autowired
    private WaveDataService waveDataService;

    @Autowired
    private FacilityWaveService facilityWaveService;

    @GetMapping("/list")
    public BaseResponse<List<WaveData>> listData(@RequestParam("facilityId") Long facilityId) {
        LambdaQueryWrapper<FacilityWave> eq = new QueryWrapper<FacilityWave>().lambda()
                .eq(FacilityWave::getFacilityId, facilityId);
        List<Long> waveDataIds = facilityWaveService.list(eq).stream()
                .map(FacilityWave::getWaveDataId)
                .collect(Collectors.toList());

        LambdaQueryWrapper<WaveData> notIn = new QueryWrapper<WaveData>()
                .lambda()
                .notIn(!waveDataIds.isEmpty(),WaveData::getId, waveDataIds);

        List<WaveData> list = waveDataService.list(notIn);
        return Response.success(list);
    }

    @PostMapping("/add")
    public BaseResponse<Boolean> addData(@RequestBody  WaveData data) {
        boolean save = waveDataService.save(data);
        return save ? Response.success() : Response.error();
    }



    @GetMapping("/page/{pageNum}")
    public BaseResponse<Page<WaveData>> page(@PathVariable Long pageNum) {
        Page<WaveData> page = waveDataService.page(new Page<>(pageNum, 10));
        return Response.success(page);
    }


    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Long id) {
        boolean b = waveDataService.removeById(id);
        return  b ? Response.success() : Response.error();
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody WaveData data) {
        boolean b = waveDataService.updateById(data);
        return  b ? Response.success() : Response.error();
    }


    @PostMapping("/compare")
    public BaseResponse<Boolean> compare(@RequestParam("file") MultipartFile file,@RequestParam("facilityId") Long facilityId) {

        try {
            // 从上传文件中获取数据
            CsvReader reader = CsvUtil.getReader();
            List<WaveCompareData> compareData = reader.read(new InputStreamReader(file.getInputStream()), WaveCompareData.class);

            // 查找需要设备绑定的波形数据
            LambdaQueryWrapper<FacilityWave> eq = new QueryWrapper<FacilityWave>().lambda()
                    .eq(FacilityWave::getFacilityId, facilityId);
            List<Long> waveDataIds = facilityWaveService.list(eq).stream()
                    .map(FacilityWave::getWaveDataId)
                    .collect(Collectors.toList());

            // 查找绑定所有的波形数据
            LambdaQueryWrapper<WaveData> in = new QueryWrapper<WaveData>().lambda()
                    .in(WaveData::getId, waveDataIds);
            List<WaveData> list = waveDataService.list(in);

            boolean isMatch = false;
            // 拿到每一个比较数据
            Integer yValue = compareData.get(0).getValue();
            Date startTime = compareData.get(0).getStartTime();
            for(WaveData data : list) {
                // 找到第一个数据开始比对的位置
                String xyValue = data.getXyValue();
                List<WaveXyValue> bean = JSONUtil.toBean(xyValue, WaveXyValueList.class).getValue();

                Optional<WaveXyValue> any = bean.stream()
                        .filter(value -> value.getY().equals(yValue))
                        .findAny();
                if(any.isPresent()) {
                    int i1 = bean.indexOf(any.get());
                    boolean allMatch = true;
                    for (int i = 1 ; i < compareData.size(); i++) {
                        WaveCompareData nexData = compareData.get(i);
                        long between = DateUtil.between(startTime, nexData.getStartTime(), DateUnit.SECOND);
                        if(between != (long) data.getUnitValue() * i) {
                            allMatch = false;
                            break;
                        }

                        boolean equals = nexData.getValue().equals(bean.get((i % data.getPeriod())).getY());
                        if(!equals) {
                            allMatch = false;
                            break;
                        }
                    }
                    isMatch = allMatch ;
                }

                if(isMatch) break;
            }

            return isMatch ? Response.success() : Response.error(ErrorMessageEnum.WAVE_DATA_COMPARE_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
