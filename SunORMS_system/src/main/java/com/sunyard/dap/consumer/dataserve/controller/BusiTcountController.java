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
 * @description: 历史业务种类业务量
 * @author: yey.he
 * @create: 2020-07-16 17:00
 **/
@RestController
@Slf4j
@RequestMapping("/sundap/busiTcount")
public class BusiTcountController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/list")
    public ReturnT<List> getListType(@RequestBody Map<String,Object> params){return client.getBusiTcountListType(params);}

    @PostMapping("/listPie")
    ReturnT<Map> getListTypePie(@RequestBody Map<String,Object> params){
        return client.getListTypePie(params);
    };

    @PostMapping("/channel")
    public ReturnT<List> getChannelInfo(@RequestBody Map<String,Object> params){
        return client.getBusiTcountChannelInfo(params);
    }

    @PostMapping("/channelPie")
    public ReturnT<Map> getChannelInfoPie(@RequestBody Map<String,Object> params){
        return client.getBusiTcountChannelInfoPie(params);
    }

    @PostMapping("/branch")
    public ReturnT<List> getBranchInfo(@RequestBody Map<String,Object> params){
        return client.getBusiTcountBranchInfo(params);
    }

    @PostMapping("/site")
    public ReturnT<List> getSiteInfo(@RequestBody Map<String,Object> params){
        return client.getBusiTcountSiteInfo(params);
    }

    @PostMapping("/zone")
    public ReturnT<List> getZoneInfo(@RequestBody Map<String,Object> params){
        return client.getBusiTcountZoneInfo(params);
    }

    @PostMapping("/typeMonthly")
    public ReturnT<List> getTypeMonthly(@RequestBody Map<String,Object> params){
        return client.getBusiTcountInfoMonthly(params);
    }

}
