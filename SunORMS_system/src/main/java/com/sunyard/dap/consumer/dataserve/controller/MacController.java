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
 * @description: 自助设备
 * @author: yey.he
 * @create: 2020-08-13 09:26
 **/
@RestController
@RequestMapping("/sundap/mac")
@Slf4j
@RefreshScope
public class MacController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/listDetail")
    public ReturnT<Page<HashMap<String, Object>>> getListDetail(@RequestBody Map<String,Object> params){return client.getMacListDetail(params);}

    @PostMapping("/listFaultByTime")
    public ReturnT<List> getListFaultByTime(@RequestBody Map<String,Object> params){return client.getMacListFaultByTime(params);}

    @PostMapping("/listStatus")
    public ReturnT<List> getListStatus(@RequestBody Map<String,Object> params){return client.getMacListStatus(params);}

    @PostMapping("/listBranchFaultRate")
    public ReturnT<List> getListBranchFaultRate(@RequestBody Map<String,Object> params){
        return client.getMacListBranchFaultRate(params);
    }

    @PostMapping("/listSiteFaultRate")
    public ReturnT<List> getListSiteFaultRate(@RequestBody Map<String,Object> params){
        return client.getMacListSiteFaultRate(params);
    }


    /**
     * 设备地区分布相关
     **/

    @PostMapping("/listBranchOnlineMac")
    public ReturnT<List> getListBranchOnlineMac(@RequestBody Map<String,Object> params){return client.getMacListBranchOnlineMac(params);}

    @PostMapping("/listBranchMacFaultCount")
    public ReturnT<List> getListBranchMacFaultCount(@RequestBody Map<String,Object> params){return client.getMacListBranchMacFaultCount(params);}

    @PostMapping("/listBranchMacReplaceRate")
    public ReturnT<List> getListBranchMacReplaceRate(@RequestBody Map<String,Object> params){return client.getMacListBranchMacReplaceRate(params);}

    @PostMapping("/listMacReplaceRateMonthly")
    public ReturnT<List> getListMacReplaceRateMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacReplaceRateMonthly(params);}

    /**
     * 设备画像相关
     **/

    @PostMapping("/macProtrayal")
    public ReturnT<List> getMacProtrayal(@RequestBody Map<String,Object> params){return client.getMacProtrayal(params);}

    @PostMapping("/listMacTypeCount")
    public ReturnT<List> getListMacTypeCount(@RequestBody Map<String,Object> params){return client.getMacListMacTypeCount(params);}

    @PostMapping("/listMacTypeAssess")
    public ReturnT<List> getListMacTypeAssess(@RequestBody Map<String,Object> params){return client.getMacListMacTypeAssess(params);}

    @PostMapping("/listMacTypeFaultRate")
    public ReturnT<List> getListMacTypeFaultRate(@RequestBody Map<String,Object> params){return client.getMacListMacTypeFaultRate(params);}
    
    @PostMapping("/listMacBrandFaultRate")
    public ReturnT<List> getListMacBrandFaultRate(@RequestBody Map<String,Object> params){return client.getMacListMacBrandFaultRate(params);}

    @PostMapping("/listMacReplaceOtcRateMonthly")
    public ReturnT<List> getListMacReplaceOtcRateMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacReplaceOtcRateMonthly(params);}

    @PostMapping("/listMacTypeBusiMonthly")
    public ReturnT<List> getListMacTypeBusiMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacTypeBusiMonthly(params);}

    @PostMapping("/listMacTypeSuccessAndFaultRate")
    public ReturnT<List> getListMacTypeSuccessAndFaultRate(@RequestBody Map<String,Object> params){return client.getMacListMacTypeSuccessAndFaultRate(params);}

    @PostMapping("/listMacMaintainMonthly")
    public ReturnT<List> getListMacMaintainMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacMaintainMonthly(params);}

    @PostMapping("/listMacTypeBusiCount")
    public ReturnT<List> getListMacTypeBusiCount(@RequestBody Map<String,Object> params){return client.getMacListMacTypeBusiCount(params);}

    @PostMapping("/listMacBusiCount")
    public ReturnT<Page<HashMap<String, Object>>> getListMacBusiCount(@RequestBody Map<String, Object> params){return client.getMacListMacBusiCount(params);}



    @PostMapping("/listSiteFaultCount")
    public ReturnT<List> getListSiteFaultCount(@RequestBody Map<String,Object> params){return client.getMacListSiteFaultCount(params);}



}
