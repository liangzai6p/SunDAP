package com.sunyard.dap.consumer.dataserve.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 网点机构
 * @author: yey.he
 * @create: 2020-08-26 17:18
 **/
@RestController
@RequestMapping("/sundap/site")
@Slf4j
@RefreshScope
public class SiteController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/listBaseInfo")
    public ReturnT<List> getListBaseInfo(@RequestBody Map<String,Object> params){
        return client.getSiteListBaseInfo(params);
    }

    @PostMapping("/listSiteGrade")
    public ReturnT<List> getListSiteGrade(@RequestBody Map<String,Object> params){
        return client.getSiteListSiteGrade(params);
    }

    @PostMapping("/listTransStatus")
    public ReturnT<List> getListTransStatus(@RequestBody Map<String,Object> params){
        return client.getSiteListTransStatus(params);
    }

    @PostMapping("/listHallInfo")
    public ReturnT<List> getListHallInfo(@RequestBody Map<String,Object> params){
        return client.getSiteListHallInfo(params);
    }


    @PostMapping("/listCashInfo")
    public ReturnT<List> getListCashInfo(@RequestBody Map<String,Object> params){
        return client.getSiteListCashInfo(params);
    }

    @PostMapping("/listCashInfoBar")
    public ReturnT<Map> getListCashInfoMap(@RequestBody Map<String,Object> params){
        return client.getSiteListCashInfoMap(params);
    }

    @PostMapping("/listQueHourly")
    public ReturnT<List> getListQueHourly(@RequestBody Map<String,Object> params){
        return client.getSiteListQueHourly(params);
    }

    @PostMapping("/listQueHourlyLine")
    public ReturnT<Map> getListQueHourlyLine(@RequestBody Map<String,Object> params){
        return client.getSiteListQueHourlyLine(params);
    }

    @PostMapping("/listMacBusiTypeCountHourly")
    public ReturnT<List> getListMacBusiTypeCountHourly(@RequestBody Map<String,Object> params){
        return client.getSiteListMacBusiTypeCountHourly(params);
    }
    @PostMapping("/listMacBusiTypeCountHourlyLine")
    public ReturnT<Map> getListMacBusiTypeCountHourlyLine(@RequestBody Map<String,Object> params){
        return client.getSiteListMacBusiTypeCountHourlyLine(params);
    }

    @PostMapping("/listBranchGrade")
    public ReturnT<List> getListBranchGrade(@RequestBody Map<String,Object> params) {
        return client.getListBranchGrade(params);
    }

    @PostMapping("/listSiteSGrade")
    public ReturnT<List> getListSiteSGrade(@RequestBody Map<String,Object> params){
        return client.getListSiteSGrade(params);
    }
}
