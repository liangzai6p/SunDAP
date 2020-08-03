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
 * @description: 离线运营业务量
 * @author: yey.he
 * @create: 2020-06-12 16:00
 **/
@RestController
@Slf4j
@RequestMapping("/sundap/busiCount")
public class BusiCountController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/info")
    public ReturnT<List> getInfo(@RequestBody Map<String,Object> params){return client.getBusiCountInfo(params);}

    @PostMapping("/channel")
    public ReturnT<List> getChannelInfo(@RequestBody Map<String,Object> params){
        return client.getBusiCountChannelInfo(params);
    }

    @PostMapping("/branch")
    public ReturnT<List> getBranchInfo(@RequestBody Map<String,Object> params){
        return client.getBusiCountBranchInfo(params);
    }

    @PostMapping("/site")
    public ReturnT<List> getSiteInfo(@RequestBody Map<String,Object> params){
        return client.getBusiCountSiteInfo(params);
    }

    @PostMapping("/zone")
    public ReturnT<List> getZoneInfo(@RequestBody Map<String,Object> params){
        return client.getBusiCountZoneInfo(params);
    }

    @PostMapping("/infoMonthly")
    public ReturnT<List> getInfoMonthly(@RequestBody Map<String,Object> params){
        return client.getBusiCountInfoMonthly(params);
    }


    @PostMapping("/branchMonthly")
    public ReturnT<List> getBranchMonthly(@RequestBody Map<String,Object> params){
        return client.getBusiCountBranchMonthly(params);
    }


    @PostMapping("/zoneMonthly")
    public ReturnT<List> getZoneMonthly(@RequestBody Map<String,Object> params){
        return client.getBusiCountZoneMonthly(params);
    }


    @PostMapping("/siteMonthly")
    public ReturnT<List> getSiteMonthly(@RequestBody Map<String,Object> params){
        return client.getBusiCountSiteMonthly(params);
    }


    @PostMapping("/channelMonthly")
    public ReturnT<List> getChannelMonthly(@RequestBody Map<String,Object> params){
        return client.getBusiCountChannelMonthly(params);
    }

    @PostMapping("/branchCount")
    public ReturnT<List> getBranchCount(@RequestBody Map<String,Object> params){
        return client.getBusiCountBranchCount(params);
    }

    @PostMapping("/zoneCount")
    public ReturnT<List> getZoneCount(@RequestBody Map<String,Object> params){
        return client.getBusiCountZoneCount(params);
    }

    @PostMapping("/siteCount")
    public ReturnT<List> getSiteCount(@RequestBody Map<String,Object> params){
        return client.getBusiCountSiteCount(params);
    }

    @PostMapping("/channelCount")
    public ReturnT<List> getChannelCount(@RequestBody Map<String,Object> params){
        return client.getBusiCountChannelCount(params);
    }

}
