package com.sunyard.dap.dataserve.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.LiaoNingBusiCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/liaoning")
@Slf4j
@RefreshScope
public class LiaoNingBusiCountController {

    @Autowired
    private LiaoNingBusiCountService service;

    /**
     * 获取辽宁大屏机构业务量数据
     */
    @PostMapping("/selectBusiNum")
    public ReturnT<Map> selectBusiNum(@RequestBody Map<String,Object> params){
        return service.selectBusiNum(params);
    }

    /**
     * 获取当日每小时平均处理时长
     * @return
     */
    @PostMapping("/getHourAvgTime")
    public ReturnT<Map> getHourAvgTime(@RequestBody Map<String,Object> params){
        return service.getHourAvgTime(params);
    }

    /**
     * 获取当日每小时业务量
     * @return
     */
    @PostMapping("/getHourBusiCount")
    public ReturnT<Map> getHourBusiCount(@RequestBody Map<String,Object> params){
        return service.getHourBusiCount(params);
    }

    /**
     * 获取分状态业务量
     * @return
     */
    @PostMapping("/getTransStateCount")
    public ReturnT<Map> getTransStateCount(@RequestBody Map<String,Object> params){
        return service.getTransStateCount(params);
    }

    /**
     * 获取柜员处理业务数
     * @return
     */
    @PostMapping("/getTellerBusiCount")
    public ReturnT<List> getTellerBusiCount(@RequestBody Map<String,Object> params){
        return service.getTellerBusiCount(params);
    }

    /**
     * 获取辽宁大屏地图中地区（机构）日业务量
     * @param params
     * @return
     */
    @PostMapping("/selectOrganBusi")
    public ReturnT<List> selectOrganBusi(@RequestBody Map<String,Object> params){
        return service.selectOrganBusi(params);
    }
}
