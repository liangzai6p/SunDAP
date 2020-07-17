package com.sunyard.dap.consumer.dataserve.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 实时业务种类业务量
 * @author: yey.he
 * @create: 2020-07-17 11:09
 **/
@RestController
@Slf4j
@RequestMapping("/sundap/busiTrt")
public class BusiTrtController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/list")
    public ReturnT<List> getListType(@RequestBody Map<String,Object> params){return client.getBusiTrtListType(params);}

    @PostMapping("/channel")
    public ReturnT<List> getChannelInfo(@RequestBody Map<String,Object> params){
        return client.getBusiTrtChannelInfo(params);
    }

    @PostMapping("/branch")
    public ReturnT<List> getBranchInfo(@RequestBody Map<String,Object> params){
        return client.getBusiTrtBranchInfo(params);
    }

    @PostMapping("/site")
    public ReturnT<List> getSiteInfo(@RequestBody Map<String,Object> params){
        return client.getBusiTrtSiteInfo(params);
    }

    @PostMapping("/zone")
    public ReturnT<List> getZoneInfo(@RequestBody Map<String,Object> params){
        return client.getBusiTrtZoneInfo(params);
    }

    @PostMapping("/typeHourly")
    public ReturnT<List> getTypeMonthly(@RequestBody Map<String,Object> params){
        return client.getBusiTrtInfoHourly(params);
    }

}
