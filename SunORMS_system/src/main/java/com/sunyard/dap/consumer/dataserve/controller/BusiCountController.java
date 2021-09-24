package com.sunyard.dap.consumer.dataserve.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

    @PostMapping("/allCount")
    public ReturnT<List> getAllCount(@RequestBody Map<String,Object> params){return client.getBusiCountAllCount(params);}

    @PostMapping("/allDaily")
    public ReturnT<List> getAllDaily(@RequestBody Map<String,Object> params){return client.getBusiCountAllDaily(params);}

    @PostMapping("/allDailyMsg")
    public ReturnT<Map> getAllDailyMsg(@RequestBody Map<String,Object> params){return client.getBusiCountAllDailyMsg(params);}

    @PostMapping("/channelDaily")
    public ReturnT<List> getChannelDaily(@RequestBody Map<String,Object> params){
        return client.getBusiCountChannelDaily(params);
    }

    @PostMapping("/branchDaily")
    public ReturnT<List> getBranchDaily(@RequestBody Map<String,Object> params){
        return client.getBusiCountBranchDaily(params);
    }

    @PostMapping("/branchDailyLine")
    public ReturnT<Map> getBranchDailyLine(@RequestBody Map<String,Object> params){
        return client.getBusiCountBranchDailyLine(params);
    }

    @PostMapping("/siteDaily")
    public ReturnT<List> getSiteDaily(@RequestBody Map<String,Object> params){
        return client.getBusiCountSiteDaily(params);
    }

    @PostMapping("/zoneDaily")
    public ReturnT<List> getZoneDaily(@RequestBody Map<String,Object> params){
        return client.getBusiCountZoneDaily(params);
    }

    @PostMapping("/allMonthly")
    public ReturnT<List> getAllMonthly(@RequestBody Map<String,Object> params){
        return client.getBusiCountAllMonthly(params);
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

    /*@PostMapping("/channelMonthlyLine")
    public ReturnT<Map> getChannelMonthlyLine(@RequestBody Map<String,Object> params){
        return client.getBusiCountChannelMonthlyLine(params);
    }*/

    @PostMapping("/branchCount")
    public ReturnT<List> getBranchCount(@RequestBody Map<String,Object> params){
        return client.getBusiCountBranchCount(params);
    }

    @PostMapping("/zoneCount")
    public ReturnT<List> getZoneCount(@RequestBody Map<String,Object> params){
        return client.getBusiCountZoneCount(params);
    }

    @PostMapping("/zoneCountTable")
    public ReturnT<Map> getZoneCountTable(@RequestBody Map<String,Object> params){
        return client.zoneCountTable(params);
    }
    @PostMapping("/zoneCountBVDTable")
    public ReturnT<Map> zoneCountBVDTable(@RequestBody Map<String,Object> params){
        return client.zoneCountBVDTable(params);
    }
    @PostMapping("/zoneCountMap")
    public ReturnT<Map> zoneCountMap(@RequestBody Map<String,Object> params){
        return client.zoneCountMap(params);
    }

    @PostMapping("/zoneCountBVDMap")
    public ReturnT<Map> zoneCountBVDMap(@RequestBody Map<String,Object> params){
        return client.zoneCountBVDMap(params);
    }

    @PostMapping("/siteCount")
    public ReturnT<List> getSiteCount(@RequestBody Map<String,Object> params){
        return client.getBusiCountSiteCount(params);
    }

    @PostMapping("/channelCount")
    public ReturnT<List> getChannelCount(@RequestBody Map<String,Object> params){
        return client.getBusiCountChannelCount(params);
    }
    @PostMapping("/channelCountPie")
    public ReturnT<Map> channelCountPie(@RequestBody Map<String,Object> params){
        return client.channelCountPie(params);
    }
    @PostMapping("/channelCountElcPie")
    public ReturnT<Map> channelCountElcPie(@RequestBody Map<String,Object> params){
        return client.channelCountElcPie(params);
    }
    @PostMapping("/channelCountElcBar")
    public ReturnT<Map> channelCountElcBar(@RequestBody Map<String,Object> params){
        return client.channelCountElcBar(params);
    }
    @PostMapping("/branchRank")
    public ReturnT<List> getBranchRank(@RequestBody Map<String,Object> params){
        return client.getBusiCountBranchRank(params);
    }

    @PostMapping("/operateCost")
    public ReturnT<List> getOperateCost(@RequestBody Map<String,Object> params){
        return client.getBusiCountOperateCost(params);
    }

    @PostMapping("/branchBusiDetails")
    public ReturnT<Page<HashMap<String, Object>>> getBranchBusiDetails(@RequestBody Map<String, Object> params) {
        return client.getBranchBusiDetails(params);
    }
}
