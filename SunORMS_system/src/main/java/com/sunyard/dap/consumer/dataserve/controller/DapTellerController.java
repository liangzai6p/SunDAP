package com.sunyard.dap.consumer.dataserve.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
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
 * @description: 柜员信息
 * @author: yey.he
 * @create: 2020-08-14 11:00
 **/
@RestController
@RequestMapping("/sundap/teller")
@Slf4j
@RefreshScope
public class DapTellerController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/listTeller")
    public ReturnT<Page<HashMap<String, Object>>> getListTeller(@RequestBody Map<String,Object> params){return client.getTellerListTeller(params);}

    @PostMapping("/listTellerAssess")
    public ReturnT<Page<HashMap<String, Object>>> getListTellerAssess(@RequestBody Map<String,Object> params){return client.getTellerListTellerAssess(params);}
    @PostMapping("/listTellerAssessCompreScore")
    public ReturnT<Page<HashMap<String, Object>>> listTellerAssessCompreScore(@RequestBody Map<String,Object> params){
        return client.listTellerAssessCompreScore(params);
    }
    @PostMapping("/listTellerAssessChartData")
    public ReturnT<Map> listTellerAssessChartData(@RequestBody Map<String,Object> params){
        return  client.listTellerAssessChartData(params);
    }
    @PostMapping("/listTellerGrade")
    public ReturnT<Page<HashMap<String, Object>>> getListTellerGrade(@RequestBody Map<String,Object> params){return client.getTellerListTellerGrade(params);}

    @PostMapping("/listStatus")
    public ReturnT<List> getListStatus(@RequestBody Map<String,Object> params){return client.getTellerListStatus(params);}

    @PostMapping("/listBranchOffline")
    public ReturnT<List> getListBranchOffline(@RequestBody Map<String,Object> params){return client.getTellerListBranchOffline(params);}

    @PostMapping("/listBranchOfflineBarChart")
    public ReturnT<Map> listBranchOfflineBarChart(@RequestBody Map<String,Object> params){
        return  client.listBranchOfflineBarChart(params);
    }

    @PostMapping("/listSiteOffline")
    public ReturnT<List> getListSiteOffline(@RequestBody Map<String,Object> params){return client.getTellerListSiteOffline(params);}

    @PostMapping("/listSiteOfflineBar")
    public ReturnT<Map> getListSiteOfflineBar(@RequestBody Map<String,Object> params){return client.getTellerListSiteOfflineBar(params);}

    @PostMapping("/listRoleStatus")
    public ReturnT<List> getListRoleStatus(@RequestBody Map<String,Object> params){return client.getTellerListRoleStatus(params);}

    @PostMapping("/listRoleStatusBar")
    public ReturnT<Map> getListRoleStatusBar(@RequestBody Map<String,Object> params){return client.getTellerListRoleStatusBar(params);}

    @PostMapping("/listTellerRank")
    public ReturnT<Page<HashMap<String, Object>>> getListTellerRank(@RequestBody Map<String,Object> params){return client.getTellerListTellerRank(params);}

    @PostMapping("/listTellerRankBar")
    public ReturnT<Map> getListTellerRankBar(@RequestBody Map<String,Object> params){return client.getTellerListTellerRankBar(params);}

    @PostMapping("/listByBranch")
    public ReturnT<List> listByBranch(@RequestBody Map<String,Object> params){
        return client.listByBranch(params);
    }

    @PostMapping("/listTellerErrorDetails")
    public ReturnT<Page<HashMap<String, Object>>> getListTellerErrorDetails(@RequestBody Map<String,Object> params){
        return client.getListTellerErrorDetails(params);
    }
}
