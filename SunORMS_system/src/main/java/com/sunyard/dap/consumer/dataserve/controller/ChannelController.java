package com.sunyard.dap.consumer.dataserve.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @program: SunDAP
 * @description: 渠道
 * @author: yey.he
 * @create: 2020-08-28 16:21
 **/

@RestController
@RequestMapping("/sundap/channel")
@Slf4j
@RefreshScope
public class ChannelController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/findEleAllRplRate")
    public ReturnT<List> getFindEleAllRplRate(@RequestBody Map<String,Object> params){
        return client.getChannelFindEleAllRplRate(params);
    }

    @PostMapping("/listEleRplRateByBranch")
    public ReturnT<List> getListEleRplRateByBranch(@RequestBody Map<String,Object> params){
        return client.getChannelListEleRplRateByBranch(params);
    }

    @PostMapping("/listEleAllRplRateDaily")
    public ReturnT<List> getListEleAllRplRateDaily(@RequestBody Map<String,Object> params){
        return client.getChannelListEleAllRplRateDaily(params);
    }

    @PostMapping("/listEleAllRplRateMonthly")
    public ReturnT<List> getListEleAllRplRateMonthly(@RequestBody Map<String,Object> params){
        return client.getChannelListEleAllRplRateMonthly(params);
    }

    @PostMapping("/listEleGradeByChannel")
    public ReturnT<List> getListEleGradeByChannel(@RequestBody Map<String,Object> params){
        return client.getChannelListEleGradeByChannel(params);
    }

    @PostMapping("/listEleSatisByChannel")
    public ReturnT<List> getListEleSatisByChannel(@RequestBody Map<String,Object> params){
        return client.getChannelListEleSatisByChannel(params);
    }

    @PostMapping("/listEleRplRateByChannel")
    public ReturnT<List> getListEleRplRateByChannel(@RequestBody Map<String,Object> params){
        return client.getChannelListEleRplRateByChannel(params);
    }

    @PostMapping("/listCusCountByChannel")
    public ReturnT<List> getListCusCountByChannel(@RequestBody Map<String,Object> params){
        return client.getChannelListCusCountByChannel(params);
    }

}
