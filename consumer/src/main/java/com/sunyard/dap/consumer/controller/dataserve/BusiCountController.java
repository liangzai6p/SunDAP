package com.sunyard.dap.consumer.controller.dataserve;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.feign.dataserve.BusiCountClient;
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
    private BusiCountClient client;

    @PostMapping("/info")
    public ReturnT<List> getInfo(@RequestBody Map<String,Object> params){return client.getInfo(params);}

    @PostMapping("/channel")
    public ReturnT<List> getChannelInfo(@RequestBody Map<String,Object> params){
        return client.getChannelInfo(params);
    }

    @PostMapping("/branch")
    public ReturnT<List> getBranchInfo(@RequestBody Map<String,Object> params){
        return client.getBranchInfo(params);
    }

    @PostMapping("/site")
    public ReturnT<List> getSiteInfo(@RequestBody Map<String,Object> params){
        return client.getSiteInfo(params);
    }

    @PostMapping("/zone")
    public ReturnT<List> getZoneInfo(@RequestBody Map<String,Object> params){
        return client.getZoneInfo(params);
    }

    @PostMapping("/infoMonthly")
    public ReturnT<List> getInfoMonthly(@RequestBody Map<String,Object> params){
        return client.getInfoMonthly(params);
    }


    @PostMapping("/branchMonthly")
    public ReturnT<List> getBranchMonthly(@RequestBody Map<String,Object> params){
        return client.getBranchMonthly(params);
    }


    @PostMapping("/zoneMonthly")
    public ReturnT<List> getZoneMonthly(@RequestBody Map<String,Object> params){
        return client.getZoneMonthly(params);
    }


    @PostMapping("/siteMonthly")
    public ReturnT<List> getSiteMonthly(@RequestBody Map<String,Object> params){
        return client.getSiteMonthly(params);
    }


    @PostMapping("/channelMonthly")
    public ReturnT<List> getChannelMonthly(@RequestBody Map<String,Object> params){
        return client.getChannelMonthly(params);
    }

    @PostMapping("/totalMinRT")
    public ReturnT<List> getTotalMinRT(@RequestBody Map<String,Object> params){
        return client.getTotalMinRT(params);
    }


    @PostMapping("/totalHourRT")
    public ReturnT<List> getTotalHourRT(@RequestBody Map<String,Object> params){
        return client.getTotalHourRT(params);
    }

    @PostMapping("/totalDayRT")
    public ReturnT<List> getTotalDayRT(@RequestBody Map<String,Object> params){
        return client.getTotalDayRT(params);
    }
}
